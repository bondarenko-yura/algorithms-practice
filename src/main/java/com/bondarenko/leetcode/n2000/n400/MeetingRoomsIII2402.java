package com.bondarenko.leetcode.n2000.n400;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/*
 URL: https://leetcode.com/problems/meeting-rooms-iii
 Time: x
 Space: x
 */
public class MeetingRoomsIII2402 {

	public int mostBooked(int n, int[][] meetings) {
		Arrays.sort(meetings, Comparator.comparingInt(a -> a[0]));

		var rooms = new PriorityQueue<Integer>();
		for (int i = 0; i < n; i++)
			rooms.add(i);

		// { end, room }
		var inUse = new PriorityQueue<int[]>((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);

		var useCount = new int[n];
		for (int[] m : meetings) {
			while (!inUse.isEmpty() && inUse.peek()[0] <= m[0])
				rooms.add(inUse.poll()[1]);

			var meetingStart = m[0];
			if (rooms.isEmpty()) {
				var room = inUse.poll();
				meetingStart = room[0];
				rooms.add(room[1]);
			}

			var room = rooms.poll();
			useCount[room]++;

			var meetingDuration = m[1] - m[0];
			inUse.add(new int[] { meetingStart + meetingDuration, room});
		}

		var roomIdx = 0;
		for (int r = 0; r < useCount.length; r++)
			if (useCount[roomIdx] < useCount[r])
				roomIdx = r;

		return roomIdx;
	}

}
