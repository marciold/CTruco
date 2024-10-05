/*
 *  Copyright (C) 2023 Adriann Paranhos - IFSP/SCL and Emanuel José da Silva - IFSP/SCL
 *  Contact: adriann <dot> paranhos <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: emanuel <dot> silva <at> aluno <dot> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.adriann.emanuel.armageddon;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;

import static org.assertj.core.api.Assertions.*;

public class ArmageddonTest {

    private Armageddon armageddon;
    private GameIntel.StepBuilder intel;
    private TrucoCard vira;
    private TrucoCard opponentCard;
    private List<TrucoCard> botCards;

    private GameIntel.StepBuilder maoDeOnze(List<TrucoCard> botCards, TrucoCard vira){
        return GameIntel.StepBuilder.with().gameInfo(List.of(),List.of(),vira,1)
                .botInfo(botCards,11).opponentScore(0);
    }

    private GameIntel.StepBuilder firstRoundFirstToPlay(List<TrucoCard> botCards, TrucoCard vira){
        return GameIntel.StepBuilder.with().gameInfo(List.of(),List.of(),vira,1)
                .botInfo(botCards,1).opponentScore(1);
    }

    private GameIntel.StepBuilder firstRoundSecondToPlay(List<TrucoCard> botCards,
                                                         TrucoCard vira, TrucoCard opponentCard){

        return GameIntel.StepBuilder.with().gameInfo(List.of(),List.of(),vira,1)
                .botInfo(botCards,1).opponentScore(1).opponentCard(opponentCard);
    }

    @BeforeEach
    void setUp(){
        armageddon = new Armageddon();
    }

    @Nested
    @DisplayName("Tests to implement mao de onze logic")
    class MaoDeOnzeTest{
        @Test
        @DisplayName("Should refuse mao de onze when the hand is weak")
        void shouldRefuseMaoDeOnzeHandWeak(){
            vira = TrucoCard.of(KING,DIAMONDS);
            botCards = List.of(
                    TrucoCard.of(FOUR,DIAMONDS),
                    TrucoCard.of(FIVE,HEARTS),
                    TrucoCard.of(SIX,SPADES)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isFalse();
        }

        @Test
        @DisplayName("Should accept mao de onze when the hand is strong")
        void shouldAcceptMaoDeOnzeHandStrong(){
            vira = TrucoCard.of(KING,DIAMONDS);
            botCards = List.of(
                    TrucoCard.of(ACE,DIAMONDS),
                    TrucoCard.of(THREE,SPADES),
                    TrucoCard.of(ACE,CLUBS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isTrue();
        }

        @Test
        @DisplayName("Should refuse mao de onze when don't have manilha or three")
        void shouldRefuseMaoDeOnzeWithoutThreeOrManilha(){
            vira = TrucoCard.of(FOUR,SPADES);
            botCards = List.of(
                    TrucoCard.of(SEVEN,DIAMONDS),
                    TrucoCard.of(TWO,SPADES),
                    TrucoCard.of(TWO,CLUBS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isFalse();
        }

        @Test
        @DisplayName("Should accept mao de onze when have two manilhas")
        void shouldAcceptMaoDeOnzeWithTwoManilhas(){
            vira = TrucoCard.of(TWO,CLUBS);
            botCards = List.of(
                    TrucoCard.of(THREE,HEARTS),
                    TrucoCard.of(THREE,SPADES),
                    TrucoCard.of(QUEEN,CLUBS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isTrue();
        }

        @Test
        @DisplayName("Should accept mao de onze when have manilha and three")
        void shouldAcceptMaoDeOnzeWithManilhaAndThree(){
            vira = TrucoCard.of(JACK,CLUBS);
            botCards = List.of(
                    TrucoCard.of(THREE,HEARTS),
                    TrucoCard.of(KING,SPADES),
                    TrucoCard.of(FIVE,DIAMONDS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isTrue();
        }

        @Test
        @DisplayName("Should accept mao de onze when the hand contains two and three with three being manilha")
        void shouldAcceptMaoDeOnzeWithTwoAndThreeWhenThreeIsManilha(){
            vira = TrucoCard.of(TWO,CLUBS);
            botCards = List.of(
                    TrucoCard.of(THREE,DIAMONDS),
                    TrucoCard.of(TWO,HEARTS),
                    TrucoCard.of(SEVEN,DIAMONDS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isTrue();
        }

        @Test
        @DisplayName("should refuse mao de onze when the hand contains only one three")
        void shouldRefuseMaoDeOnzeWithOnlyOneThree(){
            vira = TrucoCard.of(SEVEN,CLUBS);
            botCards = List.of(
                    TrucoCard.of(THREE,DIAMONDS),
                    TrucoCard.of(TWO,HEARTS),
                    TrucoCard.of(SEVEN,DIAMONDS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isFalse();
        }

        @Test
        @DisplayName("should refuse mao de onze when the hand contains only one manilha")
        void shouldRefuseMaoDeOnzeWithOnlyOneManilha(){
            vira = TrucoCard.of(JACK,SPADES);
            botCards = List.of(
                    TrucoCard.of(KING,SPADES),
                    TrucoCard.of(SIX,HEARTS),
                    TrucoCard.of(QUEEN,DIAMONDS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isFalse();
        }
    }

    @Nested
    @DisplayName("Tests to implement choose card logic")
    class ChooseCardTest{

        @Nested
        @DisplayName("Tests to implement logic of choose card in the first round")
        class FirstRoundChoose{

            @Nested
            @DisplayName("Tests to implement logic of choose card in the first round when is first to play")
            class FirstRoundFirstToPlayChoose{

                @Test
                @DisplayName("Should play the weakest card when have a higher couple")
                void shouldPlayWeakestWithHigherCouple(){
                    vira = TrucoCard.of(JACK,DIAMONDS);
                    botCards = List.of(
                            TrucoCard.of(KING,HEARTS),
                            TrucoCard.of(KING,CLUBS),
                            TrucoCard.of(SIX,DIAMONDS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("Should play the strongest card when the the hand is weak")
                void shouldPlayStrongestCardWeakHand(){
                    vira = TrucoCard.of(ACE,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(FIVE,HEARTS),
                            TrucoCard.of(FOUR,CLUBS),
                            TrucoCard.of(SIX,DIAMONDS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("Should play the strongest card when the hand is average and don't have manilha")
                void shouldPlayStrongestMidHand(){
                    vira = TrucoCard.of(SEVEN,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(SEVEN,HEARTS),
                            TrucoCard.of(KING,CLUBS),
                            TrucoCard.of(TWO,DIAMONDS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("Should play three when have this and manilha at same hand")
                void shouldPlayThreeWhenHaveThreeAndManilha(){
                    vira = TrucoCard.of(QUEEN,DIAMONDS);
                    botCards = List.of(
                            TrucoCard.of(JACK,HEARTS),
                            TrucoCard.of(THREE,CLUBS),
                            TrucoCard.of(SIX,DIAMONDS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("Should play the middle card when have three as manilha")
                void shouldPlayMiddleWhenThreeIsManilha(){
                    vira = TrucoCard.of(TWO,DIAMONDS);
                    botCards = List.of(
                            TrucoCard.of(ACE,SPADES),
                            TrucoCard.of(THREE,CLUBS),
                            TrucoCard.of(FIVE,DIAMONDS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should play the weakest manilha when have two")
                void shouldPlayWeakestManilha(){
                    vira = TrucoCard.of(SIX,DIAMONDS);
                    botCards = List.of(
                            TrucoCard.of(SEVEN,DIAMONDS),
                            TrucoCard.of(QUEEN,CLUBS),
                            TrucoCard.of(SEVEN,HEARTS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }
            }

            @Nested
            @DisplayName("Tests to implement logic of choose card in the first round when is second to play")
            class FirstRoundSecondToPlayChoose{

                @Test
                @DisplayName("Draw the point when have zap")
                void shouldDrawWhenZap(){
                    vira = TrucoCard.of(ACE,DIAMONDS);
                    opponentCard = TrucoCard.of(SEVEN,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(SEVEN,HEARTS),
                            TrucoCard.of(TWO,CLUBS),
                            TrucoCard.of(FOUR,DIAMONDS));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should play the weakest card when can't beat the opponent card")
                void shouldPlayWeakestCardWhenCantWin(){
                    vira = TrucoCard.of(SEVEN,DIAMONDS);
                    opponentCard = TrucoCard.of(THREE,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(FIVE,HEARTS),
                            TrucoCard.of(TWO,CLUBS),
                            TrucoCard.of(KING,DIAMONDS));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should play the weakest card possible when can beat the opponent card")
                void shouldPlayWeakestCardWhenCanWin(){
                    vira = TrucoCard.of(FOUR,DIAMONDS);
                    opponentCard = TrucoCard.of(SIX,SPADES);
                    botCards = List.of(
                            TrucoCard.of(SEVEN,HEARTS),
                            TrucoCard.of(TWO,CLUBS),
                            TrucoCard.of(KING,DIAMONDS));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should play the middle card when can beat and weakest card can't beat the opponent card")
                void shouldPlayMiddleWhenWeakestCardCantWin(){
                    vira = TrucoCard.of(FIVE,DIAMONDS);
                    opponentCard = TrucoCard.of(JACK,SPADES);
                    botCards = List.of(
                            TrucoCard.of(SEVEN,HEARTS),
                            TrucoCard.of(TWO,CLUBS),
                            TrucoCard.of(KING,DIAMONDS));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("Should play the weakest card when have high couple")
                void shouldPlayWeakestWhenHaveHighCouple(){
                    vira = TrucoCard.of(SEVEN,DIAMONDS);
                    opponentCard = TrucoCard.of(TWO,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(QUEEN,CLUBS),
                            TrucoCard.of(ACE,HEARTS),
                            TrucoCard.of(QUEEN,HEARTS));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("Should draw when possible and has one manilha")
                void shouldDrawWhenHaveOneManilha(){
                    vira = TrucoCard.of(FOUR,DIAMONDS);
                    opponentCard = TrucoCard.of(ACE,SPADES);
                    botCards = List.of(
                            TrucoCard.of(KING,CLUBS),
                            TrucoCard.of(ACE,HEARTS),
                            TrucoCard.of(FIVE,SPADES));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }
            }
        }
    }
}

