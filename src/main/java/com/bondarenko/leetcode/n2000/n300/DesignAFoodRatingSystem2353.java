package com.bondarenko.leetcode.n2000.n300;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/*
 URL: https://leetcode.com/problems/design-a-food-rating-system
 */
public class DesignAFoodRatingSystem2353 {

	static class FoodRatings {

		private final Map<String, Rating> foods = new HashMap<>();
		private final Map<String, TreeSet<Rating>> cuisines = new HashMap<>();

		/*
		 Time: NLogN
		 Space: N
		*/
		public FoodRatings(String[] foods, String[] cuisines, int[] ratings) {
			for (int i = 0; i < foods.length; i++) {
				var rt = new Rating(cuisines[i], foods[i], ratings[i]);
				this.foods.put(foods[i], rt);
				this.cuisines.computeIfAbsent(cuisines[i], v -> new TreeSet<>()).add(rt);
			}
		}

		/*
		 Time: LogN
		 Space: 1
		*/
		public void changeRating(String food, int newRating) {
			var f = this.foods.get(food);
			var cuisine = this.cuisines.get(f.cuisine);
			cuisine.remove(f);
			f.rating = newRating;
			cuisine.add(f);
		}

		/*
		 Time: 1
		 Space: 1
		*/
		public String highestRated(String cuisine) {
			return this.cuisines.get(cuisine).first().food;
		}

		private static final class Rating implements Comparable<Rating> {

			final String cuisine;
			final String food;
			int rating;

			private Rating(String cuisine, String food, int rating) {
				this.cuisine = cuisine;
				this.food = food;
				this.rating = rating;
			}

			@Override
			public int compareTo(Rating that) {
				var cmp = that.rating - this.rating;
				if (cmp != 0)
					return cmp;
				return this.food.compareTo(that.food);
			}

		}

	}

}
