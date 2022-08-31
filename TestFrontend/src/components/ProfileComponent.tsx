import { Component } from "react";
import { Redirect } from "react-router-dom";
import AuthService from "../services/auth.service";
import IUser from "../types/user.type";
import "../App.css"

type Props = {};

type State = {
  redirect: string | null,
  userReady: boolean,
  currentUser: IUser & { accessToken: string }
}
export default class Profile extends Component<Props, State> {
  constructor(props: Props) {
    super(props);

    this.state = {
      redirect: null,
      userReady: false,
      currentUser: { accessToken: "" }
    };
  }

  componentDidMount() {
    const currentUser = AuthService.getCurrentUser();

    if (!currentUser) this.setState({ redirect: "/home" });
    this.setState({ currentUser: currentUser, userReady: true })
  }

  render() {
    if (this.state.redirect) {
      return <Redirect to={this.state.redirect} />
    }

    const { currentUser } = this.state;

    return (
      <div id="card-container">
        {(this.state.userReady) ?
          <div className="card card-container">
            <header>
              <img
                  src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
                  alt="profile-img"
                  className="profile-img-card"
              /><br/>
              <h3>
                <strong>{currentUser.username?.toUpperCase()}</strong> Profili
              </h3><br/>
            </header>
            <p>
              <strong>Adı:</strong>{" "}
              {currentUser.username}
            </p><br/>
            <p>
              <strong>Email:</strong>{" "}
              {currentUser.email}
            </p><br/>
          </div> : null}
      </div>
    );
  }
}
