import { Component } from "react";
import { Switch, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AuthService from "./services/auth.service";
import IUser from "./types/user.type";

import Login from "./components/LoginComponent";
import Register from "./components/RegisterComponent";
import Profile from "./components/ProfileComponent";

import EventBus from "./common/EventBus";

import AddNews from "./components/news/add-news.component";
import NewsList from "./components/news/news-list.component";
import NewsComponent from "./components/news/news.component";
import AddNotice from "./components/notice/add-notice.component";
import NoticeComponent from "./components/notice/notice.component";
import NoticeList from "./components/notice/notice-list.component";

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
            <div className="navbar-nav mr-auto">
              <li className="nav-item">
                <Link to={"/newsList"} className="nav-link">
                  Haberler
                </Link>
              </li>
            </div>
            {currentUser ? (
                    <div className="navbar-nav mr-auto">
                      <li className="nav-item">
                        <Link to={"/addNews"} className="nav-link">
                          HaberEkle
                        </Link>
                      </li>
                    </div>):
                (<div></div>)}
            <div className="navbar-nav mr-auto">
              <li className="nav-item">
                <Link to={"/noticeList"} className="nav-link">
                  Duyurular
                </Link>
              </li>
            </div>
            {currentUser ? (
                    <div className="navbar-nav mr-auto">
                      <li className="nav-item">
                        <Link to={"/addNotice"} className="nav-link">
                          DuyuruEkle
                        </Link>
                      </li>
                    </div>):
                (<div></div>)}

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
            <Route exact path="/login" component={Login} />
            <Route exact path="/register" component={Register} />
            <Route exact path="/profile" component={Profile} />

            <Route exact path={["/", "/newsList"]} component={NewsList} />
            <Route exact path="/addNews" component={AddNews} />
            <Route path="/news/:id" component={NewsComponent} />

            <Route exact path={["/noticeList"]} component={NoticeList} />
            <Route exact path="/addNotice" component={AddNotice} />
            <Route path="/notices/:id" component={NoticeComponent} />

          </Switch>
        </div>

        { /*<AuthVerify logOut={this.logOut}/> */}
      </div>
    );
  }
}

export default App;