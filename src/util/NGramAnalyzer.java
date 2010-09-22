package util;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

public class NGramAnalyzer extends Analyzer {
    private int minGram = 1;
    private int maxGram = 3;
    public NGramAnalyzer(int minGram, int maxGram) {
        this.minGram = minGram;
        this.maxGram = maxGram;
    }
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return new NGramTokenizer(reader, minGram, maxGram);
    }
}