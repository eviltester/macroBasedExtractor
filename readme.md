# Macro Based Source Code Extractor

I started writing this back in 2013 to help me write Java For Testers and some other code based material I was working on.

Parts of this were taken from the "Evil Tester Markup Language" processor that I used to create the 2nd Edition of [Selenium Simplified](https://github.com/eviltester/seleniumSimplifiedRCBook).

I wrote this because I was sick of copy and pasting code from the IDE into the text of the book.

Instead I wanted to:

- write the code
- add some 'markup' in the code to identify 'chunks'
- have something that pulled out all those 'chunks' of code into text files
- when I wrote the book it would reference those text files

So that:

- the code in the book was always up to date
- no copy and paste errors

I did have a look at other, existing text based macro systems but they didn't quite do what I wanted and given their 'generic' nature were more complicated and sophisticated than I needed.

This extractor also creates a copy of the source code with all of the macros removed so it is easy to create a distributable version of the source.

I've used this to create

- [Java For Testers](https://www.compendiumdev.co.uk/page/javafortestersbook)
- [Automating and Testing a REST API](https://www.compendiumdev.co.uk/page/tracksrestapibook)

And some other pdf based material.

This, in conjunction with [Pandocifier](https://github.com/eviltester/pandocifier) are my offline book creation system. I use Leanpub to create the final printed versions.

I wrote this for my use, and it has evolved over time. And since it was for me it doesn't necessarily have a lot of documentation and error reporting could easily be improved.

Use `mvn clean package` to build it from source.

Copyright Alan Richardson, Compendium Developments Ltd 2013-2018

---

* [www.eviltester.com](http://www.eviltester.com)
* [www.youtube.com/user/EviltesterVideos](https://www.youtube.com/user/EviltesterVideos)
* [www.compendiumdev.co.uk](http://www.compendiumdev.co.uk)
* [www.JavaForTesters.com](http://javafortesters.com)
* [www.SeleniumSimplified.com](https://seleniumsimplified.com)

---

- Linkedin - [@eviltester](https://uk.linkedin.com/in/eviltester)
- Twitter - [@eviltester](https://twitter.com/eviltester)
- Instagram - [@eviltester](https://www.instagram.com/eviltester)
- Facebook - [@eviltester](https://facebook.com/eviltester/)
- Youtube - [EvilTesterVideos](https://www.youtube.com/user/EviltesterVideos)
- Pinterest - [@eviltester](https://uk.pinterest.com/eviltester/)
- Github - [@eviltester](https://github.com/eviltester/)
- Slideshare - [@eviltester](www.slideshare.net/eviltester)


---

TODO:

- I do need to tidy up the tests to generate output files in sensible places so be aware of noise in the source folders if you do run the tests.