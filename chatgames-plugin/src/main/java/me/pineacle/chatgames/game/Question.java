package me.pineacle.chatgames.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Question {

    @Getter public final Game game;
    @Getter public final String question;
    @Getter public final List<String> answers;

}
