
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router-dom';
import "./login.css";
import PhoneInput from "react-phone-input-2";
import apicall1 from '../../apicall/apicall1';
import { useDispatch, useSelector } from 'react-redux';
import { setLoading, setShowPassword, setUser } from '../../redux/slices/userSlice';

function Login() {
const dispatch=useDispatch()
const {user,loading,showPassword} = useSelector((state) => state.pessenger);
    const navigate = useNavigate();
    function loginUser() {
        if (user.phoneNumber !== '' && user.password !== '') {
            dispatch(setLoading(true));
            apicall1("/auth/login","POST",user).then((res) => {
                    dispatch(setLoading(false));
                    if (res.data) {
                        localStorage.setItem('access_token', res.data.access_token);
                        localStorage.setItem('refresh_token', res.data.refresh_token);
                        dispatch(setUser({ phoneNumber: '', password: '' }));
                        if (res.data.role==="ROLE_ADMIN") {
                            navigate("/bosh_sahifa");

                        }else if(res.data.role==="ROLE_DRIVER"){
                            navigate("/yo'nalish");
                        }
                    } else {
                        toast.error("Yaroqsiz parol yoki yaroqsiz username.");
                    }
                })
                .catch(() => {
                    dispatch(setLoading(false));
                    toast.error("Tizimga kirishda xatolik yuz berdi.");
                });
        } else {
            toast.warning("Iltimos, barcha maydonlarni toʻldiring.");
        }
    }

    return (
        <div className={"big_mean"} >
            <div className="card1">
                <ToastContainer toastStyle={{ backgroundColor: 'white', color: 'black' }} autoClose={1000} />

                <h1 >Login</h1>
                <div className="input-field">
                    <PhoneInput
                        inputClass={"input-field"}
                        country={"uz"}
                        onChange={(e) => dispatch(setUser({ ...user, phoneNumber: '+' + e }))}
                        value={user.phoneNumber}
                    />
                </div>
                <div className="input-field">
                    <input
                        onChange={(e) => dispatch(setUser({ ...user, password: e.target.value }))}
                        value={user.password}
                        placeholder="Parol..."
                        type={showPassword ? "text" : "password"} // Toggle between text and password
                    />
                    <span 
                        className="eye-icon"
                        onClick={() => dispatch(setShowPassword(!showPassword))}
                    >
                        {showPassword ? '🙈' : '👁️'}
                    </span>
                </div>
                <button onClick={loginUser} className="buttonS">
                    {loading ? "Loading..." : "Login to your Account!"}
                </button>
            </div>
        </div>
    );
}

export default Login;
