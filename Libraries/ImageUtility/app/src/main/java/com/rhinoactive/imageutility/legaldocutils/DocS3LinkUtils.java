package com.rhinoactive.imageutility.legaldocutils;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hunter on 2017-09-12.
 */

public class DocS3LinkUtils {

    public static void setLegalDocLinkClickListeners(View view, Activity activity, final String docS3Path, String fileProviderAuth) {
        final LegalDocCallback legalDocCallback = new LegalDocCallback(activity, docS3Path, fileProviderAuth);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                legalDocCallback.getAndDisplayLegalDoc();
            }
        });
    }

    public static void setLegalDocLinkSpannableString(TextView legalDocsTextView, String precedingText, LegalDocNameAndPath terms,
                                                      LegalDocNameAndPath privacy, String fileProviderAuth, Activity activity) {
        SpannableString precedingString = new SpannableString(precedingText);
        LegalDocCallback termsDocCallback = new LegalDocCallback(activity, terms.getPath(), fileProviderAuth);
        LegalDocCallback privacyDocCallback = new LegalDocCallback(activity, privacy.getPath(), fileProviderAuth);

        SpannableString termsString = createSpannableLinkString(terms.getName(), termsDocCallback);
        SpannableString privacyString = createSpannableLinkString(privacy.getName(), privacyDocCallback);

        SpannedString entireText = (SpannedString) android.text.TextUtils.concat(precedingString, " ", termsString, " and ", privacyString, ".");
        legalDocsTextView.setText(entireText);
        legalDocsTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static SpannableString createSpannableLinkString(String docName, LegalDocCallback legalDocCallback) {
        SpannableString spannableString = new SpannableString(docName);
        ClickableSpan clickableSpan = createClickableSpan(legalDocCallback);
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private static ClickableSpan createClickableSpan(final LegalDocCallback legalDocCallback) {
        return new ClickableSpan() {
            @Override
            public void onClick(View view) {
                legalDocCallback.getAndDisplayLegalDoc();
            }
        };
    }
}
