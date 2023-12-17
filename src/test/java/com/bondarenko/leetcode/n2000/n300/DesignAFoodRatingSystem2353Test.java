package com.bondarenko.leetcode.n2000.n300;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DesignAFoodRatingSystem2353Test {

	@Test
	void test() {
		var foodRatings = new DesignAFoodRatingSystem2353.FoodRatings(
				new String[]{"kimchi", "miso", "sushi", "moussaka", "ramen", "bulgogi"},
				new String[]{"korean", "japanese", "japanese", "greek", "japanese", "korean"},
				new int[]{9, 12, 8, 15, 14, 7});
		assertThat(foodRatings.highestRated("korean")).isEqualTo("kimchi");
		assertThat(foodRatings.highestRated("japanese")).isEqualTo("ramen");
		foodRatings.changeRating("sushi", 16);
		assertThat(foodRatings.highestRated("japanese")).isEqualTo("sushi");
		foodRatings.changeRating("ramen", 16);
		assertThat(foodRatings.highestRated("japanese")).isEqualTo("ramen");
	}

}
