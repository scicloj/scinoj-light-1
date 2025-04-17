(ns render
  (:require [scicloj.clay.v2.api :as clay]
            [scicloj.clay.v2.server]
            [clj-yaml.core :as yaml]
            [clojure.java.shell :as shell]
            [babashka.fs :as fs]))

(defn loading! []
  (scicloj.clay.v2.server/loading!))

(defn make-website-pages! []
  (clay/make! {:format [:quarto :html]
               :base-source-path "notebooks"
               :source-path ["index.clj"
                             "speakers.clj"
                             "hosts.clj"
                             "sessions.clj"
                             "schedule.clj"]
               :run-quarto false
               :show false
               :base-target-path "website"}))

(defn quarto-config []
  (yaml/generate-string
   {:project {:type :website}
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
                    :toc true}}}))

(defn write-quarto-config! []
  (spit "website/_quarto.yml"
        (quarto-config)))

(defn quarto-render! []
  (->> (shell/with-sh-dir
         "website"
         (shell/sh "quarto" "render"))
       ((juxt :err :out))
       (mapv println)))

(defn move-to-docs! []
  (babashka.fs/delete-tree "docs")
  (babashka.fs/copy-tree "website/_site" "docs")
  (babashka.fs/delete-tree "website/_site"))

(defn show! []
  (scicloj.clay.v2.server/update-page!
   {:show true
    :browse true
    :base-target-path "docs"
    :full-target-path "docs/index.html"}))

(defn render-website! []
  (make-website-pages!)
  (write-quarto-config!)
  (quarto-render!)
  (move-to-docs!))

(defn render-and-show-website! []
  (loading!)
  (render-website!)
  (show!))

(comment
  (render-and-show-website!))
