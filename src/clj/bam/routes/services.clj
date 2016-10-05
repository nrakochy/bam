(ns bam.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [bam.db.repo-service :as repo :refer [retrieve]]
            [schema.core :as s]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :refer [authenticated?]]))

(defn access-error [_ _]
  (unauthorized {:error "unauthorized"}))

(defn wrap-restricted [handler rule]
  (restrict handler {:handler  rule
                     :on-error access-error}))

(defmethod restructure-param :auth-rules
  [_ rule acc]
  (update-in acc [:middleware] conj [wrap-restricted rule]))

(defmethod restructure-param :current-user
  [_ binding acc]
  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))

(defapi service-routes
        {:swagger {:ui   "/api-docs"
                   :spec "/swagger.json"
                   :data {:info {:version     "1.0.0"
                                 :title       "BAM API"
                                 :description "Ye olde api"}}}}

        (GET "/authenticated" []
             :auth-rules authenticated?
             :current-user user
             (ok {:user user}))

        (context "/api" []
                 (context "/users" []
                          :tags ["users"]
                          (GET "/:username" []
                               :path-params [username :- String]
                               :summary "Retrieve user by username"
                               (ok (repo/retrieve {:user/username username})))

                          (GET "/:id" []
                               :path-params [id :- Long]
                               :summary "Retrieve user by db id"
                               (ok (repo/retrieve {:user/user-id id}))))

                 (context "/projects" []
                          :tags ["projects"]
                          (GET "/:id" []
                               :path-params [id :- Long]
                               :summary "Retrieve user by db id"
                               (ok (repo/retrieve {:project/project-id id}))))

                 (context "/organizations" []
                          :tags ["organizations"]
                          (GET "/:id" []
                               :path-params [id :- Long]
                               :summary "Get organization by id"
                               (ok (repo/retrieve {:organization/org-id id}))))

                 (context "/command" []
                          :tags ["commands"]
                          (POST "/" []
                               :body-params [command :- String payload :- {s/Keyword String}]
                               :summary "Server command writer"
                               (ok (repo/retrieve payload))))))
