;; # Hosts

;; Here are the conference hosts/emcees:

^{:clay {:hide-code true}}
(ns hosts
  (:require [scicloj.kindly.v4.kind :as kind]
            [utils]))

(let [{:keys [hosts people]} utils/info]
  (->> hosts
       sort
       (map (comp utils/person-md people))
       kind/fragment))



