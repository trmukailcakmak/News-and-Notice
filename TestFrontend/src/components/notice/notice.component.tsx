import { Component, ChangeEvent } from "react";
import { RouteComponentProps } from 'react-router-dom';

import NoticeDataService from "../../services/notice.service";
import INoticeData from "../../types/notice.type";

interface RouterProps { // type for `match.params`
  id: string; // must be type `string` since value comes from the URL
}

type Props = RouteComponentProps<RouterProps>;

type State = {
  current: INoticeData;
  message: string;
}

export default class NoticeComponent extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.onChangeTitle = this.onChangeTitle.bind(this);
    this.onChangeDescription = this.onChangeDescription.bind(this);
    this.getNews = this.getNews.bind(this);
    this.updatePublished = this.updatePublished.bind(this);
    this.update = this.update.bind(this);
    this.delete = this.delete.bind(this);

    this.state = {
      current: {
        id: null,
        title: "",
        description: "",
        published: false,
      },
      message: "",
    };
  }

  componentDidMount() {
    this.getNews(this.props.match.params.id);
  }

  onChangeTitle(e: ChangeEvent<HTMLInputElement>) {
    const title = e.target.value;

    this.setState(function (prevState) {
      return {
        current: {
          ...prevState.current,
          title: title,
        },
      };
    });
  }

  onChangeDescription(e: ChangeEvent<HTMLInputElement>) {
    const description = e.target.value;

    this.setState((prevState) => ({
      current: {
        ...prevState.current,
        description: description,
      },
    }));
  }

  getNews(id: string) {
    NoticeDataService.get(id)
      .then((response: any) => {
        this.setState({
          current: response.data,
        });
        console.log(response.data);
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  updatePublished(status: boolean) {
    const data: INoticeData = {
      id: this.state.current.id,
      title: this.state.current.title,
      description: this.state.current.description,
      published: status,
    };

    NoticeDataService.update(data, this.state.current.id)
      .then((response: any) => {
        this.setState((prevState) => ({
          current: {
            ...prevState.current,
            published: status,
          },
          message: "The status was updated successfully!"
        }));
        console.log(response.data);
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  update() {
    NoticeDataService.update(
      this.state.current,
      this.state.current.id
    )
      .then((response: any) => {
        console.log(response.data);
        this.setState({
          message: "The tutorial was updated successfully!",
        });
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  delete() {
    NoticeDataService.delete(this.state.current.id)
      .then((response: any) => {
        console.log(response.data);
        this.props.history.push("/tutorials");
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }

  render() {
    const { current } = this.state;

    return (
      <div>
        {current ? (
          <div className="edit-form">
            <h4>Notice</h4>
            <form>
              <div className="form-group">
                <label htmlFor="title">Title</label>
                <input
                  type="text"
                  className="form-control"
                  id="title"
                  value={current.title}
                  onChange={this.onChangeTitle}
                />
              </div>
              <div className="form-group">
                <label htmlFor="description">Description</label>
                <input
                  type="text"
                  className="form-control"
                  id="description"
                  value={current.description}
                  onChange={this.onChangeDescription}
                />
              </div>

              <div className="form-group">
                <label>
                  <strong>Status:</strong>
                </label>
                {current.published ? "Published" : "Pending"}
              </div>
            </form>

            {current.published ? (
              <button
                className="badge badge-primary mr-2"
                onClick={() => this.updatePublished(false)}
              >
                UnPublish
              </button>
            ) : (
              <button
                className="badge badge-primary mr-2"
                onClick={() => this.updatePublished(true)}
              >
                Publish
              </button>
            )}

            <button
              className="badge badge-danger mr-2"
              onClick={this.delete}
            >
              Delete
            </button>

            <button
              type="submit"
              className="badge badge-success"
              onClick={this.update}
            >
              Update
            </button>
            <p>{this.state.message}</p>
          </div>
        ) : (
          <div>
            <br />
            <p>Please click on a Notice...</p>
          </div>
        )}
      </div>
    );
  }
}
