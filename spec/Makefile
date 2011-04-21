#

SOURCES=spec.tex \
        styles/jsr.sty \
        styles/jsrfrontstyle.tex \
        styles/jsrmainstyle.tex \
        styles/lineno.sty \
        styles/macros.tex \
        styles/ntheorem.sty \
        chapters/titlepage.tex \
        chapters/license.tex \
        chapters/intro.tex \
        chapters/applications.tex \
        chapters/resources.tex \
        chapters/providers.tex \
        chapters/context.tex \
        chapters/environment.tex \
        chapters/delegate.tex \
        chapters/annotations.tex \
        chapters/headers.tex \
        chapters/changes.tex \
        chapters/refs.tex \
        references.bib

all: spec.pdf

spec.pdf: ${SOURCES}
	pdflatex spec.tex
	bibtex spec.aux
	pdflatex spec.tex
	pdflatex spec.tex

clean:
	rm -f spec.aux spec.bbl spec.blg spec.log spec.out spec.pdf spec.thm spec.toc
	rm -f chapters/*.aux styles/*.aux
