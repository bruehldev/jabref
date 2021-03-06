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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.jabref.importer.ParserResult;
import net.sf.jabref.model.entry.AuthorList;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.model.entry.FieldName;

/**
 * INSPEC format importer.
 */
public class InspecImporter extends ImportFormat {

    private static final Pattern INSPEC_PATTERN = Pattern.compile("Record.*INSPEC.*");

    @Override
    public String getFormatName() {
        return "INSPEC";
    }

    @Override
    public List<String> getExtensions() {
        return Collections.singletonList(".txt");
    }

    @Override
    public String getDescription() {
        return "INSPEC format importer.";
    }

    @Override
    public boolean isRecognizedFormat(BufferedReader reader) throws IOException {
        // Our strategy is to look for the "PY <year>" line.
        String str;
        while ((str = reader.readLine()) != null) {
            if (INSPEC_PATTERN.matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ParserResult importDatabase(BufferedReader reader) throws IOException {
        List<BibEntry> bibitems = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            if (str.length() < 2) {
                continue;
            }
            if (str.indexOf("Record") == 0) {
                sb.append("__::__").append(str);
            } else {
                sb.append("__NEWFIELD__").append(str);
            }
        }
        String[] entries = sb.toString().split("__::__");
        String type = "";
        Map<String, String> h = new HashMap<>();
        for (String entry : entries) {
            if (entry.indexOf("Record") != 0) {
                continue;
            }
            h.clear();

            String[] fields = entry.split("__NEWFIELD__");
            for (String s : fields) {
                String f3 = s.substring(0, 2);
                String frest = s.substring(5);
                if ("TI".equals(f3)) {
                    h.put(FieldName.TITLE, frest);
                } else if ("PY".equals(f3)) {
                    h.put(FieldName.YEAR, frest);
                } else if ("AU".equals(f3)) {
                    h.put(FieldName.AUTHOR,
                            AuthorList.fixAuthorLastNameFirst(frest.replace(",-", ", ").replace(";", " and ")));
                } else if ("AB".equals(f3)) {
                    h.put(FieldName.ABSTRACT, frest);
                } else if ("ID".equals(f3)) {
                    h.put(FieldName.KEYWORDS, frest);
                } else if ("SO".equals(f3)) {
                    int m = frest.indexOf('.');
                    if (m >= 0) {
                        String jr = frest.substring(0, m);
                        h.put(FieldName.JOURNAL, jr.replace("-", " "));
                        frest = frest.substring(m);
                        m = frest.indexOf(';');
                        if (m >= 5) {
                            String yr = frest.substring(m - 5, m).trim();
                            h.put(FieldName.YEAR, yr);
                            frest = frest.substring(m);
                            m = frest.indexOf(':');
                            if (m >= 0) {
                                String pg = frest.substring(m + 1).trim();
                                h.put(FieldName.PAGES, pg);
                                String vol = frest.substring(1, m).trim();
                                h.put(FieldName.VOLUME, vol);
                            }
                        }
                    }

                } else if ("RT".equals(f3)) {
                    frest = frest.trim();
                    if ("Journal-Paper".equals(frest)) {
                        type = "article";
                    } else if ("Conference-Paper".equals(frest) || "Conference-Paper; Journal-Paper".equals(frest)) {
                        type = "inproceedings";
                    } else {
                        type = frest.replace(" ", "");
                    }
                }
            }
            BibEntry b = new BibEntry(DEFAULT_BIBTEXENTRY_ID, type); // id assumes an existing database so don't
            // create one here
            b.setField(h);

            bibitems.add(b);

        }

        return new ParserResult(bibitems);
    }
}
