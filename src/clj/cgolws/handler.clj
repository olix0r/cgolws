(ns cgolws.handler
  (:use [cgolws.core        :only (evolve-world validate-world)]
        [ring.util.response :only (resource-response)])
  (:require [clojure.edn       :as edn]
            [compojure.core    :as cpjr]
            [compojure.handler :as handler]
            [compojure.route   :as route]))

(cpjr/defroutes app-routes
  (cpjr/GET "/" []
    ;; the main event
    (resource-response "/public/index.html"))

  (cpjr/POST "/evolve" req
    ;; transform the board according to the rules
    (time
      (let [text    (slurp (req :body))
            posted  (edn/read-string text)
            world   (validate-world posted)]
        (if (contains? world :invalid)
          {:headers {"Content-Type" "application/edn"}
           :body    (pr-str world)
           :status  400}
          {:headers {"Content-Type" "application/edn"}
           :body    (pr-str (evolve-world world))
           :status  200}))))

  (route/resources "/")
  (route/not-found "EFF OFF"))

(def app
  (handler/api app-routes))
