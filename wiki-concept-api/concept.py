from wikiExtracter import Dump
import re


class ConceptPicker:
    concepts = set()


    def getConcept(self,content):
##
#   @para: content:text of an article
#   @return: a list for unique concepts in this article
#   @pick rule: useing "re" to identify [[...]] part
        content=self._strip_text(content)
        content=self._remove_ads_from_content(content)
        compiler=re.compile(r"\[\[.+?\]\]")

        allconcepts=compiler.findall(content);
        for concept in allconcepts:
            if '|' in concept:
                words= concept[2:concept.__len__()-2].split('|')
                for word in words:
                    self.concepts.add(word)
            else:
                self.concepts.add(concept[2:concept.__len__()-2]);
        return self.concepts

    def _strip_text(self, string):
        """Removed unwanted information from article test"""
        # remove citation numbers

        string = re.sub(r'\[\d+]', '', string)
        # correct spacing around fullstops + commas
        string = re.sub(r' +[.] +', '. ', string)
        string = re.sub(r' +[,] +', ', ', string)
        # remove sub heading edits tags
        string = re.sub(r'\s*\[\s*edit\s*\]\s*', '\n', string)
        # remove unwanted areas
        UNWANTED_SECTIONS = (
            'External links and resources',
            'External links',
            'Navigation menu',
            'See also',
            'References',
            'Further reading',
            'Contents',
            'Official',
            'Other',
            'Notes',
        )
        string = re.sub(
            '|'.join(UNWANTED_SECTIONS), '', string, re.I | re.M | re.S
        )
        return string


    @staticmethod
    def _remove_ads_from_content(bio_text):
        """Returns article content without references to Wikipedia"""
        pattern = r'([^.]*?Wikipedia[^.]*\.)'
        return re.sub(pattern, '', bio_text)
