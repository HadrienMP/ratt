package fr.hadrienmp.ratt.word

import com.pholser.junit.quickcheck.Property
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith
import java.util.*

@RunWith(JUnitQuickcheck::class)
class RandomWordSpec {

    @Property
    fun one_letter_words_are_vowels() {
        val randomWord = randomWord(WordLength(1))

        // THEN a vowel is picked
        assertThat(randomWord).isIn(vowels.map { Word(it) })
    }

    @Property
    fun two_letter_words() {
        // GIVEN a vowel is picked is a
        // AND a two letter word
        // AND difficulty of proncounciation in medium
        val wordLength = WordLength(2)
        val letters = FixedLetters(SingleLetter("a"), RandomLetters())

        // WHEN a word is generated
        val randomWord = RandomWords(letters).of(wordLength)

        // THEN the second letter and last letter cannot be
        val impossibleFinalLetters = "a,e,u".split(",")
        assertThat(randomWord.letterAt(1)).isNotIn(impossibleFinalLetters)
    }
}

class RandomWords(private val letters: Letters) {
    fun of(wordLength: WordLength) = Word(letters.pick())
}

interface Letters {
    fun pick(): String
}

class RandomLetters : Letters {
    override fun pick(): String = vowels[Random().nextInt(vowels.size)]
}

class SingleLetter(private val letter: String): Letters {
    override fun pick(): String = letter
}

class FixedLetters(vararg picks: Letters): Letters {
    private val picks = picks.toMutableList()
    override fun pick(): String = picks.removeAt(0).pick()
}

val vowels = listOf("a", "e", "i", "o", "u", "y")

fun randomWord(wordLength: WordLength): Word {
    val vowel = vowels[Random().nextInt(vowels.size)]
    return Word(vowel)
}

data class Word(val content: String) {
    // FIXME HMP: 15/12/2017 il faut vérifier la taille du mot
    fun letterAt(i: Int) = content[i]
    // FIXME HMP: 15/12/2017 interdire les mots vides
}

class WordLength(length: Int) {
    // FIXME HMP: 15/12/2017 limiter les entrées possibles
}
