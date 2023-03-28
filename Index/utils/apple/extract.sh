#!/bin/sh

rm -rf extractor-plus
git clone https://github.com/akfreas/emoji-extractor-plus.git extractor-plus
cd extractor-plus
python3 -m pip install -r requirements.pip
python3 extract.py --ttc_file "../Apple Color Emoji.ttc"
