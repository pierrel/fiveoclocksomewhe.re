(ns time.utils)

(defn mapper
  "Returns a map of `keys` associated to values by `f`"
  [f keys]
  (reduce (fn [acc k]
            (assoc acc
                   k
                   (f k)))
          {}
          keys))

(defn map-flip
  ([m key]
   (reduce (fn [acc val]
             (assoc acc
                    val
                    key))
           {}
           (get m key)))
  ([m]
   (reduce (fn [acc key]
             (merge acc (map-flip m key)))
           {}
           (keys m))))
