package id.soetfw.vertx.extension

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * [Documentation Here]
 *
 * @author Argi Danurahadi
 * @email danu.argi@gmail.com
 */

fun List<Regex>.regexPatterns(): String {
    return this.map { it.pattern }.reduce { i, s -> "$i - $s" }
}

fun List<Regex>.matches(input: String): Boolean {
    return this.any { it.matches(input) }
}

fun List<Regex>.containMatchIn(input: String): Boolean {
    return this.any { it.containsMatchIn(input) }
}

fun List<Regex>.find(input: String, startIndex: Int = 0): List<MatchResult> {
    return this.map { it.find(input, startIndex) }.filter { it != null }.map { it!! }
}

fun List<Regex>.findAll(input: String, startIndex: Int = 0): List<MatchResult> {
    return this.map { it.findAll(input, startIndex).toList() }.flatten()
}

fun String.r(option: RegexOption = RegexOption.IGNORE_CASE): Regex {
    return Regex(this, option)
}

/**
 * Find String with patter
 *
 * @param pattern String
 *
 * @return String
 */
fun String.extract(pattern: String, caseSensitive: Boolean = true): String {
    if (pattern.isEmpty()) {
        throw DataInconsistentException("Regex pattern is empty")
    }

    val p: Pattern = if (caseSensitive)
        Pattern.compile(pattern)
    else {
        Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
    }

    val m = p.matcher(this)

    return if (m.find()) {
        m.group()
    } else {
        ""
    }
}

data class Match(val start: Int, val end: Int, val text: String, val group: List<String> = emptyList())

infix fun Pair<Int, Int>.detail(text: String): Match {
    return Match(this.first, this.second, text)
}

infix fun Match.group(group: List<String>): Match {
    return this.copy(group = group)
}

/**
 * Matching string with pattern
 *
 * @param pattern String
 *
 * @return boolean
 */
fun String.matchWith(pattern: String): Boolean {
    if (pattern.isEmpty()) {
        throw DataInconsistentException("Regex pattern is empty")
    }
    val p: Pattern = Pattern.compile(pattern)
    val m = p.matcher(this)

    return m.find()
}

/**
 * Matching string with pattern
 *
 * @param pattern String
 *
 * @return Matcher
 */
fun String.matchTo(pattern: String): Matcher {
    if (pattern.isEmpty()) {
        throw DataInconsistentException("Regex pattern is empty")
    }
    val p: Pattern = Pattern.compile(pattern)

    return p.matcher(this)
}

fun String.insert(position: Int, text: String): String {
    val buffer = StringBuffer(this)
    buffer.insert(position, text)
    return buffer.toString()
}

fun String.insertMatchStart(match: Match, text: String): String {
    return this.insert(match.start, text)
}

fun String.insertMatchEnd(match: Match, text: String): String {
    return this.insert(match.end, text)
}

fun String.removeMatch(match: Match): String {
    return this.removeRange(match.start, match.end)
}

fun String.insertRemoveMatchEnd(match: Match, text: String): String {
    return this.insertMatchEnd(match, text).removeMatch(match)
}

fun String.insertRemovePatternEnd(pattern: String, text: String): String {
    val match = this.matchFirst(pattern) ?: throw IllegalArgumentException("Pattern $pattern doesn't match anything!")
    return this.insertMatchEnd(match, text).removeMatch(match)
}

fun String.insertPatternEnd(pattern: String, text: String): String {
    val match = this.matchFirst(pattern) ?: throw IllegalArgumentException("Pattern $pattern doesn't match anything!")
    return this.insertMatchEnd(match, text)
}

fun String.insertPatternStart(pattern: String, text: String): String {
    val match = this.matchFirst(pattern) ?: throw IllegalArgumentException("Pattern $pattern doesn't match anything!")
    return this.insertMatchStart(match, text)
}


fun String.insertRemovePatternStart(pattern: String, text: String): String {
    val match = this.matchFirst(pattern) ?: throw IllegalArgumentException("Pattern $pattern doesn't match anything!")
    return this.insertMatchStart(match, text).removeMatch(match)
}


fun String.matchFirst(pattern: String): Match? {
    val matcher = this.matchTo(pattern)
    return if (matcher.find()) {
        val groupCount = matcher.groupCount()
        val groups =
                if (groupCount < 1) {
                    emptyList<String>()
                } else {
                    (1..groupCount).map {
                        matcher.group(it)
                    }
                }
        Match(matcher.start(), matcher.end(), matcher.group(), groups)
    } else {
        null
    }
}

fun String.matchAll(pattern: String): List<Match> {
    val matcher = this.matchTo(pattern)
    val result: MutableList<Match> = arrayListOf()
    while (matcher.find()) {
        val groupCount = matcher.groupCount()
        val groups =
                if (groupCount < 1) {
                    emptyList<String>()
                } else {
                    (1..groupCount).map {
                        matcher.group(it)
                    }
                }
        result.add(Match(matcher.start(), matcher.end(), matcher.group(), groups))
    }
    return result
}

fun String.matchAllBound(pattern: String, startBoundPattern: String, endBoundPattern: String): List<Match> {
    val startBoundMatch = this.matchFirst(startBoundPattern) ?: throw IllegalArgumentException("Pattern $startBoundPattern doesn't match Anything!")
    val endBoundMatch = this.matchFirst(endBoundPattern) ?: throw IllegalArgumentException("Pattern $endBoundPattern doesn't match Anything!")
    return this.matchAll(pattern).filter {
        it.start >= startBoundMatch.end && it.end <= endBoundMatch.start
    }
}

fun String.matchFirstBound(pattern: String, startBoundPattern: String, endBoundPattern: String): Match? {
    return this.matchAllBound(pattern, startBoundPattern, endBoundPattern).firstOrNull()
}

