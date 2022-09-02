package com.bondarenko.ds.queue;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QueueTest {

	@Test
	void testLinkedListQueueOperations() {
		testQueue(new QueueLinkedListImpl<>());
	}

	@Test
	void testArrayQueueOperations() {
		testQueue(new QueueArrayImpl<>());
	}

	private void testQueue(Queue<String> stack) {
		assertThat(stack.isEmpty()).isTrue();
		assertThat(stack.size()).isEqualTo(0);

		stack.enqueue("hello");
		assertThat(stack.isEmpty()).isFalse();
		assertThat(stack.size()).isEqualTo(1);

		stack.enqueue("world");
		stack.enqueue("my friend");
		stack.enqueue("piece");
		stack.enqueue("love");
		stack.enqueue("prosperity");
		stack.enqueue("trust");
		stack.enqueue("happiness");
		assertThat(stack.isEmpty()).isFalse();
		assertThat(stack.size()).isEqualTo(8);

		assertThat(stack.dequeue()).isEqualTo("hello");
		assertThat(stack.dequeue()).isEqualTo("world");
		assertThat(stack.dequeue()).isEqualTo("my friend");
		assertThat(stack.dequeue()).isEqualTo("piece");
		assertThat(stack.dequeue()).isEqualTo("love");
		assertThat(stack.dequeue()).isEqualTo("prosperity");
		assertThat(stack.dequeue()).isEqualTo("trust");
		assertThat(stack.isEmpty()).isFalse();
		assertThat(stack.size()).isEqualTo(1);

		String world = stack.dequeue();
		assertThat(world).isEqualTo("happiness");
		assertThat(stack.isEmpty()).isTrue();
		assertThat(stack.size()).isEqualTo(0);

		String notPresent = stack.dequeue();
		assertThat(notPresent).isNull();
		assertThat(stack.isEmpty()).isTrue();
		assertThat(stack.size()).isEqualTo(0);

		long load = 5_000_000;
		for (long i = 1; i <= load; i++) {
			stack.enqueue("val" + i);
			assertThat(stack.size()).isEqualTo(i);
		}
		for (long i = 1; i <= load; i++) {
			assertThat(stack.dequeue()).isEqualTo("val" + i);
			assertThat(stack.size()).isEqualTo(load - i);
		}
	}
}
