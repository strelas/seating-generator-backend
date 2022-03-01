package factory

import entity.Player
import entity.Skill
import entity.TourSeating

class SwissSeatingFactory{
    companion object {
        fun generate(baskets: List<List<Player>>): TourSeating {
            for (basket in baskets.map { it.map { it.copy(skill = Skill.A) } }) {
                if (basket.isEmpty() || basket.size % 10 != 0) {
                    throw IllegalArgumentException("size of baskets should be divide by 10")
                }
            }
            val tours = baskets.map { TourSeatingFactory.generate(it, 1) }
            var tour = tours.first()
            for (i in 1 until tours.size) {
                tour = tour.parallelSum(tours[i])
            }
            return tour
        }

        private fun generateForOneBasket(basket: List<Player>): TourSeating {
            return TourSeatingFactory.generate(basket, roundCount = 1)
        }
    }
}