<p align = right> <img src =https://forthebadge.com/images/badges/made-with-java.svg></p>

# ProjetJavaFX
Projet Java FX OBIS 3D

![image](https://user-images.githubusercontent.com/48014645/195371638-1ad96006-1eaa-475d-a55b-399a447a96b5.png)

## Presentation

The goal of this project is to visualize data from the OBIS database in a 3D representation on the earth's surface. The OBIS database contains a list of all the reports of marine species for several years. 

## Technologies & Dependencies
- Java (JRE11) : Programming language used.
- JavaFX (v16) : A framework to create a graphical interface for desktop applications.
- ControlsFX : ControlsFX is a library of UI controls and useful API.
- ObjModelImporterJFX.jar : Enable JavaFX applications to access 3D models and scenes provided in files based on widely supported 3D formats.
- JSON-20210307.jar : To read JSON file.
## How does it work ?

The graphical interface is composed of a 3D earth which can be freely manipulated with the mouse with rotation and zoom options, and an interface that allow you to search for a specific specie and gather information about it or to discover all the species in a given position.

![image](https://user-images.githubusercontent.com/48014645/195446063-f0dcd34f-14cc-45c9-9f9a-8192160260bf.png)

Here you can search for a specific specie and the suggestion field give you an autocomplete function. In the GeoHash Precision field, you can enter a value between 1 and 8 to determine the level of precision you wish to have. The specie will then be displayed as a histogram on the earth 3D model.

![image](https://user-images.githubusercontent.com/48014645/195447184-76457f52-03b9-4666-a3d0-0b4ccdd2b560.png)

The informations are time-based so you can choose to gather information about a specie in a specific time period.

![image](https://user-images.githubusercontent.com/48014645/195447417-c831f5f3-8251-43d0-be86-f398d37b1557.png)

If you click on the earth 3D model, all the species in the coresponding GeoHash will be displayed there. You can then click on a specie to display its information.

![image](https://user-images.githubusercontent.com/48014645/195448153-e31a0325-f603-493d-a39f-4dd2c1082884.png)

This field is a dynamic legend for the 3D histogram since the population of the different species are not on the same scales.

![image](https://user-images.githubusercontent.com/48014645/195448208-c11fcebc-2a98-4ec8-94c4-f26a84993aa1.png)


## Contributors
| Name             | GitHub      |
| ---------------- | ----------- |
| Adrien FORT      | [@Adrien2209]   |
| Wassim RAHOUAL   | [@WassimRahoual] |


[@Adrien2209]: https://github.com/Adrien2209
[@WassimRahoual]: https://github.com/WassimRahoual
