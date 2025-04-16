(ns utils
  (:require [scicloj.kindly.v4.kind :as kind]
            [scicloj.kindly.v4.api :as kindly]
            [tablecloth.api :as tc]
            [clojure.string :as str]
            [clojure.edn :as edn]))

(defn info []
  (-> "info.edn"
      slurp
      edn/read-string))

(defn images-md [{:keys [height]} images]
  (->> images
       (map (fn [image]
              (format "![](notebooks/images/%s){height=%d}"
                      image
                      height)))
       (str/join "\n")))

(defn person-md [{:keys [id
                         name images bio
                         include-bio depth link image-height]
                  :or {include-bio true
                       depth 2
                       image-height 200}}]
  (format "%s %s\n\n%s\n\n%s\n"
          (str/join "" (repeat depth "#"))
          (if link
            (format "[%s](/speakers/#%s)" name (clojure.core/name id))
            (or name ""))
          (images-md {:height image-height} images)
          (or (when include-bio bio)
              "")))

(defn people-md
  ([people-ids]
   (people-md nil people-ids))
  ([params people-ids]
   (let [{:keys [people]} (info)]
     (->> people-ids
          sort
          (map (fn [id]
                 (person-md (merge (people id)
                                   {:id id}
                                   params))))
          (str/join "\n")))))
