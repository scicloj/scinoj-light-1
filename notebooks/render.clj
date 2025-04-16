(ns render
  (:require [scicloj.clay.v2.api :as clay]
            [clj-yaml.core :as yaml]))

(clay/make! {:format [:quarto :html]
             :base-source-path "notebooks"
             :source-path ["index.clj"
                           "speakers.clj"
                           "hosts.clj"]
             :run-quarto false
             :base-target-path "docs"})

(->> {:project {:type :website}
      :website {:title "SciNoj Light #1"
                :navbar {:search true
                         :left [{:href "index.qmd"
                                 :text "Home"}
                                "speakers.qmd"
                                "hosts.qmd"]}}
      :format {:html {:theme "flatly"
                      :linkcolor "#440044"
                      ;; :css "styles.css"
                      :toc true}}}
     yaml/generate-string
     (spit "docs/_quarto.yml"))

