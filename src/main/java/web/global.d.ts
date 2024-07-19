import { Module } from "@angular-wave/angular.ts/types/types";
import { RouteConfig } from "./utils/router";

// angular.ts types are still a work in progress
declare global {
  interface Window {
    angular: Module;
    routes: RouteConfig[];
    crudRoutes: RouteConfig[];
    stateService: Object;
  }
}

export {};
