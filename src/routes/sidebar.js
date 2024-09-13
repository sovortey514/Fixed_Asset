/** Icons are imported separatly to reduce build time */

import DocumentTextIcon from '@heroicons/react/24/outline/DocumentTextIcon'
import Squares2X2Icon from '@heroicons/react/24/outline/Squares2X2Icon'
import TableCellsIcon from '@heroicons/react/24/outline/TableCellsIcon'
import WalletIcon from '@heroicons/react/24/outline/WalletIcon'
import ArrowRightOnRectangleIcon from '@heroicons/react/24/outline/ArrowRightOnRectangleIcon'
import UserIcon from '@heroicons/react/24/outline/UserIcon'
import Cog6ToothIcon from '@heroicons/react/24/outline/Cog6ToothIcon'
import KeyIcon from '@heroicons/react/24/outline/KeyIcon'
import { useEffect, useState } from 'react'

const iconClasses = `h-6 w-6`
const submenuIconClasses = `h-5 w-5`


const fetchUserById = async () => {
  try {
    const user =localStorage.getItem("username");
    const response = await fetch(`http://localhost:6060/auth/user/${user}`);
    if (response.ok) {
      const userData = await response.json();
      return userData.role
    } else {
      console.error('Failed to fetch user:', response.statusText);
    }
  } catch (error) {
    console.error('Error fetching user:', error);
  }
};
const SidebarRoutes = () => {
  const [routes, setRoutes] = useState([]);

  useEffect(() => {
    const fetchRoutes = async () => {
      const userRole = await fetchUserById();
      if (userRole === "ADMIN") {
        setRoutes([
          {
            path: "/app/dashboard",
            icon: <Squares2X2Icon className={iconClasses} />,
            name: "Dashboard",
          },
          {
            path: "",
            icon: <DocumentTextIcon className={`${iconClasses} inline`} />,
            name: "Asset Management",
            submenu: [
              {
                path: "/app/assetmanagement-totalasset",
                icon: <WalletIcon className={submenuIconClasses} />,
                name: "Total Asset",
              },
              {
                path: "/app/assetmanagement-assetcount",
                icon: <DocumentTextIcon className={submenuIconClasses} />,
                name: "Audit Asset",
              },
              {
                path: "/app/assetmanagement-historyasset",
                icon: <TableCellsIcon className={submenuIconClasses} />,
                name: "History",
              },
            ],
          },
          {
            path: "/app/usermanagement",
            icon: <UserIcon className={iconClasses} />,
            name: "User Management",
          },
          {
            path: "",
            icon: <Cog6ToothIcon className={`${iconClasses} inline`} />,
            name: "Settings",
            submenu: [
              {
                path: "/app/settings-profile",
                icon: <UserIcon className={submenuIconClasses} />,
                name: "Profile",
              },
              {
                path: "/login",
                icon: <ArrowRightOnRectangleIcon className={submenuIconClasses} />,
                name: "Logout",
              },
            ],
          }
        ]);
      }else{
        setRoutes([
          {
            path: "/app/dashboard",
            icon: <Squares2X2Icon className={iconClasses} />,
            name: "Dashboard",
          },
          {
            path: "",
            icon: <DocumentTextIcon className={`${iconClasses} inline`} />,
            name: "Asset Management",
            submenu: [
            
              {
                path: "/app/assetmanagement-assetcount",
                icon: <DocumentTextIcon className={submenuIconClasses} />,
                name: "Audit Asset",
              },
            
            ],
          },
          {
            path: "",
            icon: <Cog6ToothIcon className={`${iconClasses} inline`} />,
            name: "Settings",
            submenu: [
              {
                path: "/app/settings-profile",
                icon: <UserIcon className={submenuIconClasses} />,
                name: "Profile",
              },
              {
                path: "/login",
                icon: <ArrowRightOnRectangleIcon className={submenuIconClasses} />,
                name: "Logout",
              },
            ],
          }
        ])
      }
    };

    fetchRoutes();
  }, []);

  return routes; 
};

export default SidebarRoutes;