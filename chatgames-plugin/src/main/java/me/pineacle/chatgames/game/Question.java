package me.pineacle.chatgames.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.pineacle.chatgames.user.User;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Question {

    @Getter public final Game game;
    @Getter public final String question;
    @Getter public final List<String> answers;
    @Getter @Setter private Map<User, Integer> records;

}
