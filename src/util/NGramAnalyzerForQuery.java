package util;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;

public class NGramAnalyzerForQuery extends NGramAnalyzer {
    int maxGram;
    int minGram;
    public NGramAnalyzerForQuery(int minGram, int maxGram) {
      super(minGram, maxGram);
      this.minGram = minGram;
      this.maxGram = maxGram;
    }

    public TokenStream tokenStream(String fieldName, Reader reader) {
      int read = 0;
      try {
        char[] buf = new char[maxGram];

        // ŒŸõŒê‚Ì•¶š”‚ğŠm”F‚·‚é
        while (read < maxGram) {
          int c = reader.read();
          if (c == -1) {
            break;
          }
          buf[read] = (char)c;
          read++;
        }

        if (read > 0) {
          // “Ç‚ñ‚Å‚µ‚Ü‚Á‚½•ª‚ğ–ß‚·
          PushbackReader pbReader = new PushbackReader(reader, read);
          pbReader.unread(buf, 0, read);
          reader = pbReader;
        }
      }catch (IOException e) {
        throw new RuntimeException(e);
      }

      if (read < maxGram) {
        return new NGramTokenizer(reader, read, read);
      }
      else {
        return new NGramTokenizer(reader, maxGram, maxGram);
      }
    }
  }
