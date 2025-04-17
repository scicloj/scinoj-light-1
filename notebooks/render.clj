(ns render
  (:require [scicloj.clay.v2.api :as clay]
            [scicloj.clay.v2.server]
            [clj-yaml.core :as yaml]
            [clojure.java.shell :as shell]
            [babashka.fs :as fs]))

(scicloj.clay.v2.server/loading!)

(clay/make! {:format [:quarto :html]
             :base-source-path "notebooks"
             :source-path ["index.clj"
                           "speakers.clj"
                           "hosts.clj"
                           "sessions.clj"
                           "schedule.clj"]
             :run-quarto false
             :show false
             :base-target-path "website"})

(->> {:project {:type :website}
      :website {:title "SciNoj Light #1"
                :navbar {:search true
                         :left [{:href "index.qmd"
                                 :text "Home"}
                                "speakers.qmd"
                                "hosts.qmd"
                                "sessions.qmd"
                                "schedule.qmd"]}}
      :format {:html {:theme "cosmo"
                      :linkcolor "#440044"
                      ;; :css "styles.css"
                      :toc true}}}
     yaml/generate-string
     (spit "website/_quarto.yml"))

(->> (shell/with-sh-dir
       "website"
       (shell/sh "quarto" "render"))
     ((juxt :err :out))
     (mapv println))

(babashka.fs/delete-tree "docs")

(babashka.fs/copy-tree "website/_site"
                       "docs"
                       {:replace-existing true})

(babashka.fs/delete-tree "website/_site")

(scicloj.clay.v2.server/update-page!
 {:show true
  :base-target-path "docs"
  :full-target-path "docs/index.html"})

