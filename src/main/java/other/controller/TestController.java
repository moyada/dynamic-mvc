package other.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
@RestController
public class TestController {

    public String test() {
        return "ok";
    }

    public int cal(@RequestParam("a") Integer a, @RequestParam("b") Integer b) {
        return a * b;
    }

    public String getPath(@PathVariable("path") String path, String ver) {
        return path + ver;
    }


    public static class Solution {
        /**
         * @param words: the array of string means the list of words
         * @param order: a string indicate the order of letters
         * @return: return true or false
         */
        public boolean isAlienSorted(String[] words, String order) {
            if (words.length < 1) {
                return false;
            }
            if (words.length > 100) {
                return false;
            }

            Map<Character, Integer> orderMap = new HashMap<>(order.length());

            char[] chars = order.toCharArray();
            int i = 0;
            for (char c : chars) {
                orderMap.put(c, i);
                i++;
            }


            Map<String, Integer> weightMap = new HashMap<>(words.length);
            for (String word : words) {
                int w  = 0;
                int m = 1;
                char[] chs = word.toCharArray();
                for (char ch : chs) {
                    w += orderMap.get(ch) * m;
                    m += 500;
                }
                weightMap.put(word, w);
            }


            int pre = -1;
            for (String word : words) {
                int next = weightMap.get(word);
                if (pre == -1) {
                    pre = next;
                    continue;
                }
                if (pre > next) {
                    return false;
                }
            }
            return true;
        }
    }
}
