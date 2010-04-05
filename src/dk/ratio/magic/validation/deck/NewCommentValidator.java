package dk.ratio.magic.validation.deck;

import dk.ratio.magic.domain.db.deck.Comment;
import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.repository.deck.DeckDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

public class NewCommentValidator
{
    public void validate(Comment comment, Errors errors) {
        if (StringUtils.isBlank(comment.getText())) {
            errors.rejectValue("text", "required", "required");
        }
    }
}