package org.example.pjatk_chatroom.service;

import org.example.pjatk_chatroom.domain.Message;
import org.example.pjatk_chatroom.domain.MessageDto;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class MessageService {

    private static final int MAX_HISTORY_SIZE = 200;
    private static final String ANON = "Anon";
    private static final String EMPTY_STRING = "";
    private static final int ZERO = 0;

    private final Deque<MessageDto> history = new ArrayDeque<>(MAX_HISTORY_SIZE);

    public void addMessageWithNormalization(MessageDto message) {
        if (message == null) return;

        final String author = normalize(message.author());
        final String content = safeTrim(message.content());

        synchronized (history) {
            if (history.size() >= MAX_HISTORY_SIZE) {
                history.removeFirst();
            }
            history.addLast(new MessageDto(author, content));
        }
    }

    public List<Message> lastForActiveUser(int limit, String currentUser) {
        return lastMessagesForLimit(limit).stream()
                .map(m -> toViewModel(m, currentUser))
                .toList();
    }

    private List<MessageDto> lastMessagesForLimit(int limit) {
        synchronized (history) {
            final int size = history.size();
            final int numberOfMessages = Math.max(ZERO, Math.min(limit, size));
            if (numberOfMessages == ZERO) return List.of();

            final MessageDto[] snapshot = history.toArray(new MessageDto[size]);
            final List<MessageDto> out = new ArrayList<>(numberOfMessages);
            out.addAll(Arrays.asList(snapshot).subList(size - numberOfMessages, size));
            return out;
        }
    }

    private Message toViewModel(MessageDto m, String currentUser) {
        final boolean mine = isMineMessage(m, currentUser);
        return new Message(m.author(), m.content(), mine);
    }

    private static boolean isMineMessage(MessageDto m, String currentUser) {
        return m.author() != null && m.author().equalsIgnoreCase(currentUser);
    }

    private static String normalize(String s) {
        if (!StringUtils.hasText(s)) return ANON;
        return s.trim();
    }

    private static String safeTrim(String s) {
        return (s == null) ? EMPTY_STRING : s.trim();
    }
}
