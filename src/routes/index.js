// All components mapping with path for internal routes

import { lazy } from 'react'
const Dashboard = lazy(() => import('../pages/protected/Dashboard'))
const Welcome = lazy(() => import('../pages/protected/Welcome'))
const Page404 = lazy(() => import('../pages/protected/404'))
const Blank = lazy(() => import('../pages/protected/Blank'))
const Charts = lazy(() => import('../pages/protected/Charts'))
const Leads = lazy(() => import('../pages/protected/Leads'))
const Integration = lazy(() => import('../pages/protected/Integration'))
const Calendar = lazy(() => import('../pages/protected/Calendar'))
const Team = lazy(() => import('../pages/protected/Team'))
const GettingStarted = lazy(() => import('../pages/GettingStarted'))

const TotalAsset = lazy(() => import('../pages/protected/TotalAsset'))
const AssetCount = lazy(() => import('../pages/protected/AssetCount'))
const HistoryCount = lazy(() => import('../pages/protected/HistoryCount'))
const ProfileSettings = lazy(() => import('../pages/protected/ProfileSettings'))

const routes = [
  {
    path: '/dashboard', // the url
    component: Dashboard, // view rendered
  },
  {
    path: '/welcome', // the url
    component: Welcome, // view rendered
  },
  // {
  //   path: '/leads',
  //   component: Leads,
  // },
  {
    path: '/settings-team',
    component: Team,
  },
  {
    path: '/settings-profile',
    component: ProfileSettings,
  },
  {
    path: '/calendar',
    component: Calendar,
  },

  {
    path: '/assetmanagement-assetcount',
    component:AssetCount,
  },
  {
    path: '/assetmanagement-totalasset',
    component: TotalAsset,
  },
  {
    path: '/assetmanagement-historyasset',
    component: HistoryCount,
  },
  {
    path: '/getting-started',
    component: GettingStarted,
  },

  {
    path: '/integration',
    component: Integration,
  },
  {
    path: '/charts',
    component: Charts,
  },
  {
    path: '/404',
    component: Page404,
  },
  {
    path: '/blank',
    component: Blank,
  },
]

export default routes
