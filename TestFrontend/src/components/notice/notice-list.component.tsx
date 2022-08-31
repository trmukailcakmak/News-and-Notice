import { Component, ChangeEvent } from "react";
import NoticeDataService from "../../services/notice.service";
import { Link } from "react-router-dom";
import INoticeData from '../../types/notice.type';
import IUser from "../../types/user.type";
import AuthService from "../../services/auth.service";
import Swal from "sweetalert2";
import StompClient from "react-stomp-client";

type Props = {};

type State = {
  notice: Array<INoticeData>,
  current: INoticeData | null,
  currentIndex: number,
  searchTitle: string,
  currentUser : IUser | undefined
};

export default class NoticeList extends Component<Props, State>{
  constructor(props: Props) {
    super(props);
    this.onChangeSearchTitle = this.onChangeSearchTitle.bind(this);
    this.retrieve = this.retrieve.bind(this);
    this.refreshList = this.refreshList.bind(this);
    this.setActive = this.setActive.bind(this);
    this.removeAll = this.removeAll.bind(this);
    this.searchTitle = this.searchTitle.bind(this);
    this.handleNoticeCreate = this.handleNoticeCreate.bind(this);

    this.state = {
      notice: [],
      current: null,
      currentIndex: -1,
      searchTitle: "",
      currentUser : undefined
    };
  }

  handleNoticeCreate(webSocketMessage:any) {
    const data = JSON.parse(webSocketMessage.body);
    if(data.activityType==="NOTICE") {
      this.setState({
        notice: [...this.state.notice, data]
      });
    }
  }

  componentDidMount() {
    this.retrieve();
    const user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        currentUser: user
      });
    }
  }

  onChangeSearchTitle(e: ChangeEvent<HTMLInputElement>) {
    const searchTitle = e.target.value;

    this.setState({
      searchTitle: searchTitle
    });
  }

  retrieve() {
    NoticeDataService.getAll()
      .then((response: any) => {
        this.setState({
          notice: response.data
        });
        console.log(response.data);
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  refreshList() {
    this.retrieve();
    this.setState({
      current: null,
      currentIndex: -1
    });
  }

  setActive(notice: INoticeData, index: number) {
    this.setState({
      current: notice,
      currentIndex: index
    });
  }

  removeAll() {
    NoticeDataService.deleteAll()
      .then((response: any) => {
        console.log(response.data);
        this.refreshList();
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  searchTitle() {
    this.setState({
      current: null,
      currentIndex: -1
    });

    NoticeDataService.findByTitle(this.state.searchTitle)
      .then((response: any) => {
        this.setState({
          notice: response.data
        });
        console.log(response.data);
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  popup(notice:INoticeData){
    Swal.fire({
      title: notice.title,
      text: notice.description,
      imageUrl: 'https://unsplash.it/400/200',
      imageWidth: 400,
      imageHeight: 200,
      imageAlt: 'Custom image',
    })
  }

  render() {
    const { currentUser,searchTitle, notice, current, currentIndex } = this.state;

    return (
        <StompClient
            endpoint="ws://localhost:8080/websocket"
            topic="topic/create"
            onMessage={this.handleNoticeCreate}
        >
          <div className="list row">
        <div className="col-md-8">
          <div className="input-group mb-3">
            <input
              type="text"
              className="form-control"
              placeholder="Search by title"
              value={searchTitle}
              onChange={this.onChangeSearchTitle}
            />
            <div className="input-group-append">
              <button
                className="btn btn-outline-secondary"
                type="button"
                onClick={this.searchTitle}
              >
                Search
              </button>
            </div>
          </div>
        </div>
        <div className="col-md-6">
          <h4>Notice List</h4>

          <ul className="list-group">
            {notice &&
              notice.map((notice: INoticeData, index: number) => (
                <li
                  className={
                    "list-group-item " +
                    (index === currentIndex ? "active" : "")
                  }
                  onClick={() => currentUser ? (this.setActive(notice, index)):(this.popup(notice))}
                  key={index}
                >
                  {notice.title}
                </li>
              ))}
          </ul>

          {currentUser ? (notice ? (<button
              className="m-3 btn btn-sm btn-danger"
              onClick={this.removeAll}
          >
            Remove All
          </button>):(<div/>)):(<div/>)}

        </div>
        <div className="col-md-6">
          {current ? (
            <div>
              <h4>Notice</h4>
              <div>
                <label>
                  <strong>Title:</strong>
                </label>{" "}
                {current.title}
              </div>
              <div>
                <label>
                  <strong>Description:</strong>
                </label>{" "}
                {current.description}
              </div>
              <div>
                <label>
                  <strong>Status:</strong>
                </label>{" "}
                {current.published ? "Published" : "Pending"}
              </div>

              <Link
                to={"/notices/" + current.id}
                className="badge badge-warning"
              >
                Edit
              </Link>
            </div>) : (
            <div>
              {notice ?(
              <div>
                <br />
                <p>Please click on a Notice...</p>
              </div>
              ):(<div/>)}

            </div>
          )}
        </div>
      </div>
        </StompClient>

    );
  }
}
