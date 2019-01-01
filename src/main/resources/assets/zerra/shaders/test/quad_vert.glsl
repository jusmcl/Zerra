#version 140

in vec2 position;

out vec2 pass_TextureCoords;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;

void main() {
	gl_Position = projectionMatrix * vec4(position, -10.0, 1.0);

	pass_TextureCoords = position;
}
