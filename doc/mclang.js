Prism.languages.mclang = {
    comment: /#.*/,

    string: {
        pattern: /"(?:\\.|[^"\\])*"/,
        greedy: true
    },

    variable: /\$[a-zA-Z_][a-zA-Z0-9_]*/,

    keyword: /\b(?:say|scoreboard|execute|if|else)\b/,

    number: /\b-?\d+(?:\.\d+)?\b/,

    operator: /[+\-*\/=<>!]+/,

    lin: /\b(set|add)\b/,

    punctuation: /[{}()[\],]/
};