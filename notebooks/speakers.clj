;; # Speakers

;; Here are the conference speakers:

^{:clay {:hide-code true}}
(ns speakers
  (:require [scicloj.kindly.v4.kind :as kind]
            [utils]))

(let [{:keys [speakers people]} utils/info]
  (->> speakers
       sort
       (map (comp utils/person-md people))
       kind/fragment))

