import { Route, Routes, useLocation, useNavigate } from "react-router-dom";
import { Suspense, useMemo, useEffect, lazy } from "react";
import { useDispatch } from "react-redux";
import axios from "axios";
import { deleteRoutesByTime } from "./redux/slices/routeDriver";
import { message } from "antd";
import { LanguageProvider } from "./components/language/LanguageContext";
import Loader from "./components/loader/Loader";
// Lazy loading components
const Login = lazy(() => import("./components/auth/Login"));
const Landing = lazy(() => import("./components/user/Landing"));
const AdminHeder = lazy(() => import("./components/admin/adminHeader/AdminHeder"));
const AboutDriversOne = lazy(() => import("./components/admin/AboutDriversOne"));
const RoutesUser = lazy(() => import("./components/user/RoutesUser"));
const Register = lazy(() => import("./components/auth/Register"));
const PathDriver = lazy(() => import("./components/driver/headerD/PathDriver"));
const MyselfDriver = lazy(() => import("./components/driver/headerD/MyselfDriver"));

function App() {
  const dispatch = useDispatch();
  const { pathname } = useLocation();
  const navigate = useNavigate();

  const permission = useMemo(
    () => [
      { path: "/bir_haydovchi/:userName", roles: "ROLE_ADMIN" },
      { path: "/yo'nalish", roles: "ROLE_ADMIN" },
      { path: "/bosh_sahifa", roles: "ROLE_ADMIN" },
      { path: "/o'zim_haqqimda", roles: "ROLE_DRIVER" },
      { path: "/yo'nalish", roles: "ROLE_DRIVER" },
    ],
    []
  );

  const check = useMemo(() => {
    return () => {
      for (let i = 0; i < permission.length; i++) {
        if (pathname.startsWith(permission[i].path)) {
          return permission[i].roles;
        }
      }
      return null;
    };
  }, [pathname, permission]);

  const checkRoles = useMemo(() => {
    return () => {
      const rolePerm = check();
      if (rolePerm != null) {
        axios({
          url: "http://localhost:8080/api/auth/check",
          method: "get",
          headers: { authorization: localStorage.getItem("refresh_token") },
        })
          .then((res) => {
            for (let i = 0; i < res.data.length; i++) {
              if (rolePerm.includes(res.data[i].name)) {
                return;
              }
            }

            for (let index = 0; index < res.data.length; index++) {
              if (res.data[index].name === "ROLE_ADMIN") {
                navigate("/bosh_sahifa");
              } else if (res.data[index].name === "ROLE_DRIVER") {
                navigate("/yo'nalish");
              }
            }
          })
          .catch(() => {
            navigate("/");
            localStorage.removeItem("access_token");
            localStorage.removeItem("refresh_token");
          });
      }
    };
  }, [navigate, check]);

  const delByTime = useMemo(() => {
    return () => {
      dispatch(deleteRoutesByTime())
        .unwrap()
        .then((res) => {
          // message.success(res)
        });
    };
  }, [dispatch]);

  useEffect(() => {
    delByTime();
    checkRoles();
    const interval = setInterval(() => {
      checkRoles();
    }, 60000);
    return () => clearInterval(interval);
  }, [pathname, navigate, delByTime, checkRoles]);

  return (
    <LanguageProvider>
      <Suspense fallback={<Loader/>}>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<RoutesUser />} />
          <Route path="/header" element={<Landing />} />
          <Route path="/landing" element={<Landing />} />
          <Route path="/bosh_sahifa" element={<AdminHeder />} />
          <Route path="/bir_haydovchi/:userName" element={<AboutDriversOne />} />
          <Route path="/register" element={<Register />} />
          <Route path="/yo'nalish" element={<PathDriver />} />
          <Route path="/o'zim_haqqimda" element={<MyselfDriver />} />
        
        </Routes>
     </Suspense>
    </LanguageProvider>
  );
}

export default App;
