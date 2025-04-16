(ns utils
  (:require [scicloj.kindly.v4.kind :as kind]
            [scicloj.kindly.v4.api :as kindly]
            [tablecloth.api :as tc]
            [clojure.string :as str]
            [clojure.edn :as edn]))

(def info 
  (-> "info.edn"
      slurp
      edn/read-string))

(defn images-md [alt images]
  (->> images
       (map (fn [image]
              (format "![](notebooks/images/%s){height=200}"
                      image #_alt)))
       (str/join "\n")))

(defn person-md [{:keys [name images bio]}]
  (kind/md
   (format "## %s\n%s\n\n%s\n"
           name
           (images-md name images)
           bio)))
