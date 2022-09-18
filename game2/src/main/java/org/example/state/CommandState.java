package org.example.state;

import lombok.RequiredArgsConstructor;
import org.example.asynchronous.ThreadStrategy;


public interface CommandState {
     void run();
}
