import { Component } from "react";
import { Switch, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AuthService from "./services/auth.service";
import IUser from './types/user.type';

import Login from "./components/LoginComponent";
import Register from "./components/RegisterComponent";
import News from "./components/NewsComponent";
import Profile from "./components/ProfileComponent";
import Notice from "./components/NoticeComponent";
import NoticeAdd from "./components/BoardAdmin/BoardAdminNoticeAddComponent";
import BoardTest from "./components/BoardTestComponent";
import NewsAdd from "./components/BoardAdmin/BoardAdminNewsAddComponent";

import EventBus from "./common/EventBus";

type Props = {};

type State = {
  showTestBoard: boolean,
  showAdminBoard: boolean,
  currentUser: IUser | undefined
}

class App extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.logOut = this.logOut.bind(this);

    this.state = {
      showTestBoard: false,
      showAdminBoard: false,
      currentUser: undefined,
    };
  }

  componentDidMount() {
    const user = AuthService.getCurrentUser();

    if (user) {
      this.setState({
        currentUser: user,
        showTestBoard: user.roles.includes("ROLE_TEST"),
        showAdminBoard: user.roles.includes("ROLE_ADMIN"),
      });
    }

    EventBus.on("logout", this.logOut);
  }

  componentWillUnmount() {
    EventBus.remove("logout", this.logOut);
  }

  logOut() {
    AuthService.logout();
    this.setState({
      showTestBoard: false,
      showAdminBoard: false,
      currentUser: undefined,
    });
  }

  render() {
    const { currentUser, showTestBoard, showAdminBoard } = this.state;

    return (
      <div>
        <nav className="navbar navbar-expand navbar-dark bg-dark">
          <Link to={"/"} className="navbar-brand">
            Cakmak
          </Link>
          <div className="navbar-nav mr-auto">
            <li className="nav-item">
              <Link to={"/news"} className="nav-link">
                News
              </Link>
            </li>

            {showTestBoard && (
              <li className="nav-item">
                <Link to={"/test"} className="nav-link">
                  Test Board
                </Link>
              </li>
            )}

            {showAdminBoard && (
              <li className="nav-item">
                <Link to={"/newsAdd"} className="nav-link">
                  News Add
                </Link>
              </li>
            )}

            {currentUser && (
              <li className="nav-item">
                <Link to={"/notice"} className="nav-link">
                  Notice
                </Link>
              </li>
            )}

            {showAdminBoard && (
                <li className="nav-item">
                  <Link to={"/noticeAdd"} className="nav-link">
                    Notice Add
                  </Link>
                </li>
            )}
          </div>

          {currentUser ? (
            <div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/profile"} className="nav-link">
                  {currentUser.username}
                </Link>
              </li>
              <li className="nav-item">
                <a href="/login" className="nav-link" onClick={this.logOut}>
                  LogOut
                </a>
              </li>
            </div>
          ) : (
            <div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/login"} className="nav-link">
                  Login
                </Link>
              </li>

              <li className="nav-item">
                <Link to={"/register"} className="nav-link">
                  Sign Up
                </Link>
              </li>
            </div>
          )}
        </nav>

        <div className="container mt-3">
          <Switch>
            <Route exact path={["/", "/news"]} component={News} />
            <Route exact path="/login" component={Login} />
            <Route exact path="/register" component={Register} />
            <Route exact path="/profile" component={Profile} />
            <Route path="/notice" component={Notice} />
            <Route path="/test" component={BoardTest} />
            <Route path="/newsAdd" component={NewsAdd} />
            <Route path="/noticeAdd" component={NoticeAdd} />
          </Switch>
        </div>

        { /*<AuthVerify logOut={this.logOut}/> */}
      </div>
    );
  }
}

export default App;