import React, { useState } from "react";
import styles from "../../assets/homeStats.module.css"

import Profile from "../../Images/profile.png";
import { Link } from "react-router-dom";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";


const UserSearchComponent = ({ users,searchbar }) => {
    const [searchInput, setSearchInput] = useState("");
    const [searchResults, setSearchResults] = useState([]);

    const handleSearchInput = (e) => {
        const value = e.target.value;
        setSearchInput(value);

        // Perform search logic here and update searchResults state
        // For example, you can filter the users based on the search input

        const filteredUsers = users.filter(user =>
            user.userName.toLowerCase().includes(value.toLowerCase())
        );
        setSearchResults(filteredUsers);
    }

    return (
        <div className={styles["user-search-component"]}>
            <div className="d-flex flex-column w-100 p-3 h-40 justify-content-around">
            <div>  
            <h3 className={styles.searchTitle}>Search</h3>
            <button
                  type="button"
                  className="btn btn-link text-white"
                  aria-label="Close"
                  onClick={searchbar}
                  style={{ position: "absolute", top: "10px", right: "10px", background: "none", border: "none" }}
                >
                <FontAwesomeIcon icon={faTimes} />
              </button>
            </div>
            <div className={styles["search-bar"]}>
                <input
                    type="text"
                    placeholder="Search users..."
                    value={searchInput}
                    onChange={handleSearchInput}
                    className={styles["search-input"]}
                />
            </div>
            </div>
            <div className={styles["user-list"]}>
                {searchResults.map(user => (
                    <Link  key={user.userId} style={{textDecoration:"none",color:"white"}} to={`/profile/${user.userName}`}>
                        <div className={styles["user-item"]} onClick={()=>searchbar(1)}>
                        <img src={user.pic} alt={`Avatar of ${user.userName}`} className={styles["user-avatar"]} />
                        <div className={styles.subuser}>
                        <h5 className={styles["user-name"]}>{user.userName}</h5>
                        <h5 className={styles["user-email"]}>{user.userName}</h5>
                        </div>
                    </div>
                    </Link>
                ))}
            </div>
        </div>
    );
};

export default UserSearchComponent;
