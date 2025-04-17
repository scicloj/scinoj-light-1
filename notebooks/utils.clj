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

(defn images-hiccup [{:keys [height]} images]
  (->> images
       (map (fn [image]
              [:img {:src (str "notebooks/images/" image)
                     :height height}]))
       (into [:div])))

(defn person-md [{:keys [id
                         full-name images bio
                         include-bio depth link image-height]
                  :or {include-bio true
                       depth 2
                       image-height 200}}]
  (format "%s %s\n\n%s\n\n%s\n"
          (str/join "" (repeat depth "#"))
          (if link
            (format "[%s](/speakers.html#%s)" full-name (name id))
            (or full-name ""))
          (images-md {:height image-height} images)
          (or (when include-bio bio)
              "")))

(defn person-hiccup [{:keys [id
                             full-name images bio
                             include-bio depth link image-height]
                      :or {include-bio true
                           depth 2
                           image-height 200}}]
  [(if (pos? depth)
     (keyword (str "h" depth))
     :div)
   (if link
     [:a {:href (str "/speakers.html#" (name id))}
      full-name]
     [:p full-name])
   (images-hiccup {:height image-height} images)
   (when include-bio (some-> bio kind/md))])


(defn people-md
  ([people-ids]
   (people-md nil people-ids))
  ([params people-ids]
   (let [{:keys [people]} (info)]
     (->> people-ids
          (map (fn [id]
                 (person-md (merge (people id)
                                   {:id id}
                                   params))))
          (str/join "\n")))))

(defn people-hiccup
  ([people-ids]
   (people-md nil people-ids))
  ([params people-ids]
   (let [{:keys [people]} (info)]
     (->> people-ids
          (map (fn [id]
                 (person-hiccup (merge (people id)
                                       {:id id}
                                       params))))
          (into [:div])))))
