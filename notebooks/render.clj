(ns render
  (:require [scicloj.clay.v2.api :as clay]))

(clay/make! {:format [:quarto :html]
             :base-source-path "notebooks"
             :source-path ["index.clj"
                           "speakers.clj"]
             :base-target-path "docs"
             :book {:title "SciNoj Light 1"}
             :clean-up-target-dir true})
