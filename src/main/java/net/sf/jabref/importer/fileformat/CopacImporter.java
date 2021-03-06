/*  Copyright (C) 2003-2016 JabRef contributors.
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.sf.jabref.importer.fileformat;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import net.sf.jabref.importer.ParserResult;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.model.entry.FieldName;

/**
 * Importer for COPAC format.
 *
 * Documentation can be found online at:
 *
 * http://copac.ac.uk/faq/#format
 */
public class CopacImporter extends ImportFormat {

    private static final Pattern COPAC_PATTERN = Pattern.compile("^\\s*TI- ");

    @Override
    public String getFormatName() {
        return "Copac";
    }

    @Override
    public List<String> getExtensions() {
        return Collections.singletonList(".txt");
    }

    @Override
    public String getId() {
        return "cpc";
    }

    @Override
    public String getDescription() {
        return "Importer for COPAC format.";
    }

    @Override
    public boolean isRecognizedFormat(BufferedReader reader) throws IOException {
        String str;
        while ((str = reader.readLine()) != null) {
            if (CopacImporter.COPAC_PATTERN.matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ParserResult importDatabase(BufferedReader reader) throws IOException {
        Objects.requireNonNull(reader);

        List<String> entries = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        // Preprocess entries
        String str;

        while ((str = reader.readLine()) != null) {

            if (str.length() < 4) {
                continue;
            }

            String code = str.substring(0, 4);

            if ("    ".equals(code)) {
                sb.append(' ').append(str.trim());
            } else {

                // begining of a new item
                if ("TI- ".equals(str.substring(0, 4))) {
                    if (sb.length() > 0) {
                        entries.add(sb.toString());
                    }
                    sb = new StringBuilder();
                }
                sb.append('\n').append(str);
            }
        }

        if (sb.length() > 0) {
            entries.add(sb.toString());
        }

        List<BibEntry> results = new LinkedList<>();

        for (String entry : entries) {

            // Copac does not contain enough information on the type of the
            // document. A book is assumed.
            BibEntry b = new BibEntry(DEFAULT_BIBTEXENTRY_ID, "book");

            String[] lines = entry.split("\n");

            for (String line1 : lines) {
                String line = line1.trim();
                if (line.length() < 4) {
                    continue;
                }
                String code = line.substring(0, 4);

                if ("TI- ".equals(code)) {
                    setOrAppend(b, FieldName.TITLE, line.substring(4).trim(), ", ");
                } else if ("AU- ".equals(code)) {
                    setOrAppend(b, FieldName.AUTHOR, line.substring(4).trim(), " and ");
                } else if ("PY- ".equals(code)) {
                    setOrAppend(b, FieldName.YEAR, line.substring(4).trim(), ", ");
                } else if ("PU- ".equals(code)) {
                    setOrAppend(b, FieldName.PUBLISHER, line.substring(4).trim(), ", ");
                } else if ("SE- ".equals(code)) {
                    setOrAppend(b, FieldName.SERIES, line.substring(4).trim(), ", ");
                } else if ("IS- ".equals(code)) {
                    setOrAppend(b, FieldName.ISBN, line.substring(4).trim(), ", ");
                } else if ("KW- ".equals(code)) {
                    setOrAppend(b, FieldName.KEYWORDS, line.substring(4).trim(), ", ");
                } else if ("NT- ".equals(code)) {
                    setOrAppend(b, FieldName.NOTE, line.substring(4).trim(), ", ");
                } else if ("PD- ".equals(code)) {
                    setOrAppend(b, "physicaldimensions", line.substring(4).trim(), ", ");
                } else if ("DT- ".equals(code)) {
                    setOrAppend(b, "documenttype", line.substring(4).trim(), ", ");
                } else {
                    setOrAppend(b, code.substring(0, 2), line.substring(4).trim(), ", ");
                }
            }
            results.add(b);
        }

        return new ParserResult(results);
    }

    private static void setOrAppend(BibEntry b, String field, String value, String separator) {
        if (b.hasField(field)) {
            b.setField(field, b.getFieldOptional(field).get() + separator + value);
        } else {
            b.setField(field, value);
        }
    }
}
