/** Icons are imported separatly to reduce build time */

import DocumentTextIcon from '@heroicons/react/24/outline/DocumentTextIcon'
import Squares2X2Icon from '@heroicons/react/24/outline/Squares2X2Icon'
import TableCellsIcon from '@heroicons/react/24/outline/TableCellsIcon'
import WalletIcon from '@heroicons/react/24/outline/WalletIcon'
import ArrowRightOnRectangleIcon from '@heroicons/react/24/outline/ArrowRightOnRectangleIcon'
import UserIcon from '@heroicons/react/24/outline/UserIcon'
import Cog6ToothIcon from '@heroicons/react/24/outline/Cog6ToothIcon'
import KeyIcon from '@heroicons/react/24/outline/KeyIcon'
import UsersIcon from '@heroicons/react/24/outline/UsersIcon'
const iconClasses = `h-6 w-6`
const submenuIconClasses = `h-5 w-5`

const routes = [

  {
    path: '/app/dashboard',
    icon: <Squares2X2Icon className={iconClasses}/>, 
    name: 'Dashboard',
  },
  {
    path: '', //no url needed as this has submenu
    icon: <DocumentTextIcon className={`${iconClasses} inline` }/>, // icon component
    name: 'Asset Mangement', // name that appear in Sidebar
    submenu : [
      {
        path: '/app/assetmanagement-totalasset',
        icon: <WalletIcon className={submenuIconClasses}/>,
        name: 'Total Asset',
      },
      {
        path: '/app/assetmanagement-assetcount', // url
        icon: <DocumentTextIcon className={submenuIconClasses}/>, // icon component
        name: 'Asset Count', // name that appear in Sidebar
      },
      {
        path: '/app/assetmanagement-historyasset',
        icon: <TableCellsIcon className={submenuIconClasses}/>, 
        name: 'History',
      },
    ]
  },
  // {
  //   path: '', // no url needed as this has submenu
  //   icon: <UsersIcon className={`${iconClasses} inline` }/>, // icon component
  //   name: 'User Management', // name that appears in Sidebar
  //   submenu : [
  //     {
  //       path: '/app/usermanagement-users',
  //       icon: <UserIcon className={submenuIconClasses}/>, // icon component
  //       name: 'Manage Users', // name that appears in Sidebar
  //     },
  //   ]
  // },
  {
    path: '', //no url needed as this has submenu
    icon: <Cog6ToothIcon className={`${iconClasses} inline` }/>, // icon component
    name: 'Settings', // name that appear in Sidebar
    submenu : [
      {
        path: '/app/settings-profile', //url
        icon: <UserIcon className={submenuIconClasses}/>, // icon component
        name: 'Profile', // name that appear in Sidebar
      },
      {
        path: '/register', //url
        icon: <UserIcon className={submenuIconClasses}/>, // icon component
        name: 'Register', // name that appear in Sidebar
      },
      {
        path: '/login',
        icon: <ArrowRightOnRectangleIcon className={submenuIconClasses}/>,
        name: 'Login',
      },
      {
        path: '/forgot-password',
        icon: <KeyIcon className={submenuIconClasses}/>,
        name: 'Forgot Password',
      },
     
    ]
  },
  
]

export default routes


