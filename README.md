# [Website of the SciNoj Light #1 conference](https://scicloj.github.io/scinoj-light-1/)

## Contributing

The single source of truth for the conference website is in [info.edn](./info.edn).
The bio and abstract strings are interpreted as markdown, so you can include links, etc.

Pull requests would be welcome.

The website is rendered through [Clay](https://scicloj.github.io/clay) and [Quarto](https://quarto.org/).

To render the website locally, you will need Quarto installed in your environment. Then, you can open [dev/render.clj](dev/render.clj) and run `(render-website!)`. You may also run `(render-and-show-website!)` to view the website through the Clay UI.

