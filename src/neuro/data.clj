(ns neuro.data
  (:require [clojure.string :as string]))


(defn parse-float [s]
  (Float/parseFloat s))

(defn read-data-training
  "Read the csv file, split out each line and then each number, parse the tokens and break up the numbers so that the last is the target and everything else is the feature vector."
  []
  (as-> (slurp "resources/training_norm_podaci.csv") d
        (string/split d #"\n")
        (map #(string/split % #",") d)
        (map #(map parse-float %) d)
        (map (fn [s] {:x  (vec (drop-last s))  :y (last s)}) d)))

(defn read-data-test
  "Read the csv file, split out each line and then each number, parse the tokens and break up the numbers so that the last is the target and everything else is the feature vector."
  []
  (as-> (slurp "resources/test_norm_podaci.csv") d
        (string/split d #"\n")
        (map #(string/split % #",") d)
        (map #(map parse-float %) d)
        (map (fn [s] {:x  (vec (drop-last s))  :y (last s)}) d)))
