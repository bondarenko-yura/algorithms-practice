package com.bondarenko.algo.sort;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StackTest {

  @Test
  void testLinkedListStackOperations() {
    testStack(new StackLinkedListImpl<>());
  }

  @Test
  void testArrayStackOperations() {
    testStack(new StackArrayImpl<>());
  }

  private void testStack(Stack<String> stack) {
    assertThat(stack.isEmpty()).isTrue();
    assertThat(stack.size()).isEqualTo(0);

    stack.push("hello");
    assertThat(stack.isEmpty()).isFalse();
    assertThat(stack.size()).isEqualTo(1);

    stack.push("world");
    assertThat(stack.isEmpty()).isFalse();
    assertThat(stack.size()).isEqualTo(2);

    String world = stack.pop();
    assertThat(world).isEqualTo("world");
    assertThat(stack.isEmpty()).isFalse();
    assertThat(stack.size()).isEqualTo(1);

    String hello = stack.pop();
    assertThat(hello).isEqualTo("hello");
    assertThat(stack.isEmpty()).isTrue();
    assertThat(stack.size()).isEqualTo(0);

    String notPresent = stack.pop();
    assertThat(notPresent).isNull();
    assertThat(stack.isEmpty()).isTrue();
    assertThat(stack.size()).isEqualTo(0);
  }
}