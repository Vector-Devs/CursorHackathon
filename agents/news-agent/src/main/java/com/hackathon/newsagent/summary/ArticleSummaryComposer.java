package com.hackathon.newsagent.summary;

import com.hackathon.newsagent.web.dto.CategoryAssignmentDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a short narrative summary from the article text and the top risk categories.
 */
public final class ArticleSummaryComposer {

    private ArticleSummaryComposer() {
    }

    public static String compose(String title, String body, List<CategoryAssignmentDto> topCategories) {
        StringBuilder sb = new StringBuilder();
        if (!topCategories.isEmpty()) {
            sb.append("Leading supply-chain themes: ");
            sb.append(topCategories.stream()
                    .map(CategoryAssignmentDto::categoryLabel)
                    .collect(Collectors.joining(", ")));
            sb.append(". ");
        }
        String excerpt = excerpt(body != null ? body : "", 380);
        if (!excerpt.isEmpty()) {
            sb.append(excerpt);
        } else if (title != null && !title.isBlank()) {
            sb.append(title.strip());
        }
        return sb.toString().trim();
    }

    private static String excerpt(String body, int maxLen) {
        String t = body.strip();
        if (t.isEmpty()) {
            return "";
        }
        if (t.length() <= maxLen) {
            return t;
        }
        int cut = t.lastIndexOf(' ', maxLen);
        if (cut < 40) {
            cut = maxLen;
        }
        return t.substring(0, cut) + "…";
    }
}
