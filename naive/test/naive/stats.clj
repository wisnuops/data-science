(ns naive.test.stats
  (:require
   [naive.stats :refer :all]
   [clojure.test :refer :all]))

(deftest testing-stats
  (let [single (for [i (range 1000)] (rand-int 50))
        multi (for [i (range 100)
                    j (range 100)
                    k (range 100)]
                (let [a (rand-int i)
                      b (rand-int j)
                      c (rand-int k)]
                  {:a a :b b :c c
                   :d (* a b)
                   :e "Mereka"
                   :f "PastiKamu"}))]

    ;; First, test the means
    (testing "Means for single and multiple"
      (is (= (/ (reduce + single) (count single) 1.0)
             (means single)))
      (let [{:keys [a d]} (means multi [:a :d])]
        (is (= (means (mapv :a multi)) a))
        (is (= (means (mapv :d multi)) d)))
      (is (= (->> [:a :b :c :d]
                  (map #(means (map % multi)))
                  (zipmap [:a :b :c :d]))
             (means multi [:a :b :c :d])))
      (is (== 10 (means (repeat 20 10))))
      (is (= {:a 10.0 :b 10.0 :c 10.0}
             (-> (map #(zipmap [:a :b :c] %)
                      (repeat 100 (repeat 3 10)))
                 (means [:a :b :c])))))

    ;; Next, test the frequencies
    (testing "Frequencies for single and multiple"
      (is (= (frequencies single)
             (freq single)))
      (let [{:keys [a b c d]} (freq multi [:a :b :c :d])]
        (is (= (frequencies (map :a multi)) a))
        (is (= (frequencies (map :b multi)) b))
        (is (= (frequencies (map :c multi)) c))
        (is (= (frequencies (map :d multi)) d))
        (is (= {10 100 9 100}
               (freq (concat (repeat 100 10) (repeat 100 9)))))
        (is (= {:a {10 10 9 10} :b {10 10 9 10}}
               (-> (->> (concat (repeat 10 10) (repeat 10 9))
                        (map #(hash-map :a % :b %)))
                   (freq [:a :b]))))))))

;; Next, mode test
;; TODO create the test for mode & tile







