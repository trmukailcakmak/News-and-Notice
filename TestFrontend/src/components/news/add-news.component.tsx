import { Component, ChangeEvent } from "react";
import NewsDataService from "../../services/news.service";
import INewsData from '../../types/news.type';
import Swal from "sweetalert2";
import "../../App.css"
import authHeader from "../../services/auth-header";

type Props = {};

type State = INewsData & {
  submitted: boolean
};

export default class AddNews extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.onChangeTitle = this.onChangeTitle.bind(this);
    this.onChangeDescription = this.onChangeDescription.bind(this);
    this.save = this.save.bind(this);
    this.news = this.news.bind(this);

    this.state = {
      id: null,
      title: "",
      description: "",
      published: false,
      submitted: false
    };
  }

  onChangeTitle(e: ChangeEvent<HTMLInputElement>) {
    this.setState({
      title: e.target.value
    });
  }

  onChangeDescription(e: ChangeEvent<HTMLInputElement>) {
    this.setState({
      description: e.target.value
    });
  }

  save() {
    if (this.state.title==""&&this.state.description==""){
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'the title and description cannot be empty!'
      })
    }else if(this.state.title==""){
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'the title cannot be empty!'
      })}else if (this.state.description==""){
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'the descriptin cannot be empty!'
      })}else {
    const data: INewsData = {
      title: this.state.title,
      description: this.state.description
    };

    NewsDataService.create(data)
      .then((response: any) => {
        this.setState({
          id: response.data.id,
          title: response.data.title,
          description: response.data.description,
          published: response.data.published,
          submitted: true
        });
        if (this.state.submitted==true){
          Swal.fire({
            icon: 'success',
            title: 'News Added',
            text: 'press ok to add again'
          })
        }
        console.log(response.data);
        this.news();
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }}

  news() {
    this.setState({
      id: null,
      title: "",
      description: "",
      published: false,
      submitted: false
    });
  }

  render() {
    const { submitted, title, description } = this.state;

    return (
      <div id="card-container" className="submit-form">
          <div  className="card card-container">
            <strong><h3 className="ml-auto">News Add</h3></strong>
            <div className="form-group">
              <label htmlFor="title">Title</label>
              <input
                type="text"
                className="form-control"
                id="title"
                required
                value={title}
                onChange={this.onChangeTitle}
                name="title"
              />
            </div>

            <div className="form-group">
              <label htmlFor="description">Description</label>
              <input
                type="text"
                className="form-control"
                id="description"
                required
                value={description}
                onChange={this.onChangeDescription}
                name="description"
              />
            </div><br/><br/>

            <button onClick={this.save} className="btn btn-success">
              Submit
            </button>
          </div>
      </div>
    );
  }
}
